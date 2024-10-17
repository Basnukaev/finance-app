package com.basnukaev.finance.app.config

import com.integralblue.log4jdbc.spring.Log4jdbcAutoConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile

@Configuration
@Profile("dev", "test")
@Import(Log4jdbcAutoConfiguration::class)
class JdbcLoggingConfig