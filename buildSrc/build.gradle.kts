plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-gradle-plugin:3.3.2")  // id("org.springframework.boot")
    implementation("io.spring.gradle:dependency-management-plugin:1.1.6") // id("io.spring.dependency-management")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.24") // kotlin("jvm")
    implementation("org.jetbrains.kotlin:kotlin-allopen:1.9.24") // kotlin("plugin.spring")
    implementation("org.jetbrains.kotlin:kotlin-noarg:1.9.24") // kotlin("plugin.jpa")
}
