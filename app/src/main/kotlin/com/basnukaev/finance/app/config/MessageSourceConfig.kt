package com.basnukaev.finance.app.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource

@Configuration
class MessageSourceConfig {

    @Bean
    fun messageSource(): ResourceBundleMessageSource = ResourceBundleMessageSource().apply {
        setBasename("messages")
        setDefaultEncoding("UTF-8")
        setFallbackToSystemLocale(false)
        setDefaultLocale(AppLocale.RU)
    }

}