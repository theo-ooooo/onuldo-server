package com.onuldo.adapter.outbound.storage

import com.onuldo.common.exception.CustomException
import com.onuldo.common.exception.ErrorCode
import com.onuldo.port.outbound.storage.FileStorage
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest
import software.amazon.awssdk.services.s3.model.HeadObjectRequest
import java.time.Duration

@Component("s3FileStorage")
class S3FileStorage(
    @Value("\${aws.s3.bucket}")
    private val bucketName: String,
    @Value("\${aws.region:ap-northeast-2}")
    private val region: String
) : FileStorage {

    private val s3Client: S3Client = S3Client.builder()
        .region(Region.of(region))
        .credentialsProvider(DefaultCredentialsProvider.create())
        .build()

    private val s3Presigner: S3Presigner = S3Presigner.builder()
        .region(Region.of(region))
        .credentialsProvider(DefaultCredentialsProvider.create())
        .build()

    override fun saveFile(file: MultipartFile, directory: String, fileName: String): String {
        val key = "$directory/$fileName"
        val putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .contentType(file.contentType)
            .build()

        try {
            s3Client.putObject(putObjectRequest, software.amazon.awssdk.core.sync.RequestBody.fromInputStream(file.inputStream, file.size))
            return key
        } catch (e: Exception) {
            throw CustomException(ErrorCode.COMMON_INTERNAL_SERVER_ERROR, "S3 파일 저장에 실패했습니다: ${e.message}")
        }
    }

    override fun saveFile(bytes: ByteArray, directory: String, fileName: String, contentType: String): String {
        val key = "$directory/$fileName"
        val putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .contentType(contentType)
            .build()

        try {
            s3Client.putObject(putObjectRequest, software.amazon.awssdk.core.sync.RequestBody.fromBytes(bytes))
            return key
        } catch (e: Exception) {
            throw CustomException(ErrorCode.COMMON_INTERNAL_SERVER_ERROR, "S3 파일 저장에 실패했습니다: ${e.message}")
        }
    }

    /**
     * Presigned URL을 생성하여 반환합니다.
     * 클라이언트가 직접 S3에 업로드할 수 있도록 합니다.
     */
    fun generatePresignedUploadUrl(key: String, contentType: String, expirationMinutes: Int = 5): String {
        val putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .contentType(contentType)
            .build()

        val presignRequest = PutObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(expirationMinutes.toLong()))
            .putObjectRequest(putObjectRequest)
            .build()

        val presignedRequest = s3Presigner.presignPutObject(presignRequest)
        return presignedRequest.url().toString()
    }

    /**
     * Presigned URL을 생성하여 반환합니다 (다운로드용).
     */
    fun generatePresignedDownloadUrl(key: String, expirationMinutes: Int = 60): String {
        val getObjectRequest = software.amazon.awssdk.services.s3.model.GetObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .build()

        val presignRequest = software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(expirationMinutes.toLong()))
            .getObjectRequest(getObjectRequest)
            .build()

        val presignedRequest = s3Presigner.presignGetObject(presignRequest)
        return presignedRequest.url().toString()
    }

    override fun deleteFile(fileUrl: String) {
        try {
            val deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileUrl)
                .build()
            s3Client.deleteObject(deleteObjectRequest)
        } catch (e: Exception) {
            // 파일 삭제 실패는 로그만 남기고 예외를 던지지 않음
        }
    }

    override fun exists(fileUrl: String): Boolean {
        return try {
            val headObjectRequest = HeadObjectRequest.builder()
                .bucket(bucketName)
                .key(fileUrl)
                .build()
            s3Client.headObject(headObjectRequest)
            true
        } catch (e: Exception) {
            false
        }
    }
}

