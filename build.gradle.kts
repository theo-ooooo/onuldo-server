plugins {
    kotlin("jvm") version "2.2.21"
    kotlin("plugin.spring") version "2.2.21"
    id("org.springframework.boot") version "4.0.1"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("plugin.jpa") version "2.2.21"
    kotlin("kapt") version "2.2.21"
}

group = "com.onuldo"
version = "0.0.1-SNAPSHOT"
description = "onuldo"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // JWT
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    // Redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // QueryDSL
    implementation("com.querydsl:querydsl-jpa:5.1.0:jakarta")
    kapt("com.querydsl:querydsl-apt:5.1.0:jakarta")
    kapt("jakarta.annotation:jakarta.annotation-api")
    kapt("jakarta.persistence:jakarta.persistence-api")

    // Swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.0")

    // H2
    runtimeOnly("com.h2database:h2")

    // AWS S3
    implementation("software.amazon.awssdk:s3:2.20.0")
    implementation("software.amazon.awssdk:auth:2.20.0")

    // WebP 이미지 처리
    implementation("com.twelvemonkeys.imageio:imageio-webp:3.10.1")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}


kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// QueryDSL Q 클래스 생성 경로
sourceSets["main"].java.srcDirs("$buildDir/generated/source/kapt/main")

// Kapt 설정
kapt {
    arguments {
        arg("querydsl.entityAccessors", "true")
    }
}
