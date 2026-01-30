package com.onuldo.adapter.outbound.storage

import com.onuldo.common.exception.CustomException
import com.onuldo.common.exception.ErrorCode
import com.onuldo.port.outbound.storage.FileStorage
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

@Component("localFileStorage")
class LocalFileStorage(
    @Value("\${file.upload.dir:uploads}")
    private val uploadDir: String
) : FileStorage {

    init {
        val uploadPath = Paths.get(uploadDir)
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath)
        }
    }

    override fun saveFile(file: MultipartFile, directory: String, fileName: String): String {
        try {
            val dirPath = Paths.get(uploadDir, directory)
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath)
            }

            val filePath = dirPath.resolve(fileName)
            Files.copy(file.inputStream, filePath, StandardCopyOption.REPLACE_EXISTING)

            // URL 반환 (로컬 파일 시스템의 경우 상대 경로 반환)
            return "/$directory/$fileName"
        } catch (e: Exception) {
            throw CustomException(ErrorCode.COMMON_INTERNAL_SERVER_ERROR, "파일 저장에 실패했습니다: ${e.message}")
        }
    }

    override fun saveFile(bytes: ByteArray, directory: String, fileName: String, contentType: String): String {
        try {
            val dirPath = Paths.get(uploadDir, directory)
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath)
            }

            val filePath = dirPath.resolve(fileName)
            Files.write(filePath, bytes)

            // URL 반환 (로컬 파일 시스템의 경우 상대 경로 반환)
            return "/$directory/$fileName"
        } catch (e: Exception) {
            throw CustomException(ErrorCode.COMMON_INTERNAL_SERVER_ERROR, "파일 저장에 실패했습니다: ${e.message}")
        }
    }

    override fun deleteFile(fileUrl: String) {
        try {
            val filePath = Paths.get(uploadDir, fileUrl.removePrefix("/"))
            if (Files.exists(filePath)) {
                Files.delete(filePath)
            }
        } catch (e: Exception) {
            // 파일 삭제 실패는 로그만 남기고 예외를 던지지 않음
            // throw CustomException(ErrorCode.COMMON_INTERNAL_SERVER_ERROR, "파일 삭제에 실패했습니다: ${e.message}")
        }
    }

    override fun exists(fileUrl: String): Boolean {
        val filePath = Paths.get(uploadDir, fileUrl.removePrefix("/"))
        return Files.exists(filePath)
    }
}

