package com.basnukaev.finance.app.config

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisPassword
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializer

@Configuration
class RedisConfig {

    @Bean
    fun redisStandAloneConfiguration(redisProperties: RedisProperties): RedisStandaloneConfiguration {
        val configuration = RedisStandaloneConfiguration()
        configuration.hostName = redisProperties.host
        configuration.port = redisProperties.port
        configuration.username = redisProperties.username
        configuration.password = RedisPassword.of(redisProperties.password)
        return configuration
    }

    @Bean
    fun redisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, Any> {
        val objectMapper = ObjectMapper().apply {
            findAndRegisterModules()
            setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL)
            activateDefaultTyping(polymorphicTypeValidator, ObjectMapper.DefaultTyping.NON_FINAL)
        }

        val jsonSerializer = GenericJackson2JsonRedisSerializer(objectMapper)

        val redisTemplate = RedisTemplate<String, Any>().apply {
            connectionFactory = redisConnectionFactory
            keySerializer = RedisSerializer.string()
            valueSerializer = jsonSerializer
            hashKeySerializer = RedisSerializer.string()
            hashValueSerializer = jsonSerializer
        }
        redisTemplate.afterPropertiesSet()
        return redisTemplate
    }
}