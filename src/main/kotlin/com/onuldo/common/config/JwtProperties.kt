package com.onuldo.common.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    val accessTokenValidityInSeconds: Long = 3600,
    val refreshTokenValidityInSeconds: Long = 604800,
    val secret: String? = null
)


