@file:Suppress("UnstableApiUsage")

import com.basnukaev.Version.EU_VENDELI_TELEGRAM
import com.basnukaev.Version.LOG_4_JDBC
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.springframework.boot.gradle.plugin.SpringBootPlugin

group = "com.basnukaev"
version = "0.0.1"

plugins {
    id("io.spring.dependency-management")
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom(SpringBootPlugin.BOM_COORDINATES)
    }
    dependencies {
        dependency("com.integralblue:log4jdbc-spring-boot-starter:$LOG_4_JDBC")
        dependency("eu.vendeli:telegram-bot:$EU_VENDELI_TELEGRAM")
        dependency("eu.vendeli:ktgram-utils:$EU_VENDELI_TELEGRAM")
        dependency("eu.vendeli:ksp:$EU_VENDELI_TELEGRAM")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
}

allOpen {
    annotations(
        "jakarta.persistence.Entity",
        "jakarta.persistence.MappedSuperclass",
        "jakarta.persistence.Embeddable"
    )
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-Xlint:deprecation")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
