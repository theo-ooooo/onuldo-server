package com.onuldo.port.outbound.storage

import org.springframework.web.multipart.MultipartFile

/**
 * 파일 저장소 인터페이스
 */
interface FileStorage {
    /**
     * 파일을 저장하고 URL을 반환합니다.
     */
    fun saveFile(file: MultipartFile, directory: String, fileName: String): String

    /**
     * 바이트 배열을 파일로 저장하고 URL을 반환합니다.
     */
    fun saveFile(bytes: ByteArray, directory: String, fileName: String, contentType: String): String

    /**
     * 파일을 삭제합니다.
     */
    fun deleteFile(fileUrl: String)

    /**
     * 파일이 존재하는지 확인합니다.
     */
    fun exists(fileUrl: String): Boolean
}

