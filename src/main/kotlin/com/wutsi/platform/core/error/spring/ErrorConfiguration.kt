package com.wutsi.platform.core.error.spring

import com.wutsi.platform.core.logging.KVLogger
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class ErrorConfiguration(
    private val logger: KVLogger
) {
    @Bean
    open fun restControllerErrorHandler(): RestControllerErrorHandler =
        RestControllerErrorHandler(logger)
}
