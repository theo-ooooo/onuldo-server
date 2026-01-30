package com.onuldo

import com.onuldo.common.config.JwtProperties
import com.onuldo.common.config.S3Properties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties::class, S3Properties::class)
class OnuldoApplication

fun main(args: Array<String>) {
    runApplication<OnuldoApplication>(*args)
}
