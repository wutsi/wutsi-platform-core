package com.wutsi.platform.core.logging.spring

import com.wutsi.platform.core.logging.DefaultKVLogger
import com.wutsi.platform.core.logging.KVLogger
import com.wutsi.platform.core.logging.KVLoggerFilter
import com.wutsi.platform.core.logging.NullKVLogger
import com.wutsi.platform.core.tracing.TracingContext
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode

@Configuration
open class LoggingConfiguration(
    private val context: ApplicationContext,
    private val tracingContext: TracingContext
) {
    @Bean
    open fun loggingFilter(): KVLoggerFilter =
        KVLoggerFilter(logger(), tracingContext)

    @Bean
    open fun logger(): KVLogger =
        SpringKVLogger(context, nullLogger())

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
    open fun requestLogger(): DefaultKVLogger =
        DefaultKVLogger()

    @Bean
    open fun nullLogger(): NullKVLogger =
        NullKVLogger()
}
