package com.wutsi.platform.core.error.spring

import com.wutsi.platform.core.logging.KVLogger
import com.wutsi.platform.core.tracing.TracingContext
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource

@Configuration
open class ErrorConfiguration(
    private val logger: KVLogger,
    private val tracingContext: TracingContext
) {
    @Bean
    open fun restControllerErrorHandler(): RestControllerErrorHandler =
        RestControllerErrorHandler(messageSource(), logger, tracingContext)

    @Bean
    open fun messageSource(): MessageSource {
        val messageSource = ResourceBundleMessageSource()
        messageSource.setBasename("errors")
        messageSource.setDefaultEncoding("UTF-8")
        return messageSource
    }
}
