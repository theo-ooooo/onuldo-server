package com.onuldo.common.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Onuldo API")
                    .description("오늘도, 나를 위해 한 걸음 - 취미 활동을 기록하고 공유하는 타이머 기반 기록 앱 API")
                    .version("0.0.1-SNAPSHOT")
                    .contact(
                        Contact()
                            .name("Onuldo Team")
                            .email("contact@onuldo.com")
                    )
                    .license(
                        License()
                            .name("Apache 2.0")
                            .url("https://www.apache.org/licenses/LICENSE-2.0.html")
                    )
            )
    }
}

