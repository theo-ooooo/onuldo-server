package com.onuldo.common.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "fcm")
data class FcmProperties(
    /**
     * Firebase 서비스 계정 키 파일 경로
     * 또는 GOOGLE_APPLICATION_CREDENTIALS 환경 변수 사용 가능
     */
    val serviceAccountKeyPath: String? = null
)


