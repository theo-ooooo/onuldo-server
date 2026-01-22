package com.onuldo.common.config

import com.onuldo.common.security.JwtAccessDeniedHandler
import com.onuldo.common.security.JwtAuthenticationEntryPoint
import com.onuldo.common.security.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint,
    private val jwtAccessDeniedHandler: JwtAccessDeniedHandler
) {

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            // CSRF 비활성화 (JWT는 stateless이므로 불필요)
            .csrf(CsrfConfigurer<HttpSecurity>::disable)
            
            // CORS 설정
            .cors { it.configurationSource(corsConfigurationSource()) }
            
            // Form Login 비활성화 (JWT 사용)
            .formLogin(FormLoginConfigurer<HttpSecurity>::disable)
            
            // HTTP Basic 인증 비활성화
            .httpBasic(HttpBasicConfigurer<HttpSecurity>::disable)
            
            // 세션 비활성화 (JWT는 stateless)
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            
            // 인증/인가 예외 처리
            .exceptionHandling {
                it.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                it.accessDeniedHandler(jwtAccessDeniedHandler)
            }
            
            // 인가 규칙 설정
            .authorizeHttpRequests {
                it
                    // 공개 엔드포인트
                    .requestMatchers(
                        HttpMethod.POST,
                        "/api/auth/signup",
                        "/api/auth/login",
                        "/api/auth/refresh"
                    ).permitAll()
                    .requestMatchers(
                        HttpMethod.GET,
                        "/api/health"
                    ).permitAll()
                    // Swagger/OpenAPI 문서 (모든 HTTP 메서드 허용)
                    .requestMatchers(
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/swagger-ui/index.html",
                        "/v3/api-docs",
                        "/v3/api-docs/**",
                        "/api-docs",
                        "/api-docs/**",
                        "/swagger-resources/**",
                        "/webjars/**"
                    ).permitAll()
                    // 나머지 요청은 인증 필요
                    .anyRequest().authenticated()
            }
            
            // JWT 필터 추가
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    /**
     * CORS 설정
     */
    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration().apply {
            allowedOriginPatterns = listOf("*")
            allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
            allowedHeaders = listOf("*")
            exposedHeaders = listOf("Authorization", "Content-Type")
            allowCredentials = true
            maxAge = 3600L
        }

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/api/**", configuration)
        return source
    }
}


