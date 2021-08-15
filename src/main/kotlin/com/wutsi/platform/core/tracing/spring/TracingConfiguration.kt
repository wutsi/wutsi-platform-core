package com.wutsi.platform.core.tracing.spring

import com.wutsi.platform.core.tracing.FeignTracingRequestInterceptor
import com.wutsi.platform.core.tracing.TracingContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class TracingConfiguration(
    @Autowired private val context: ApplicationContext,
    @Value("\${wutsi.platform.tracing.client-id}") private val clientId: String
) {
    @Bean
    open fun tracingContext(): TracingContext =
        SpringTracingContext(context)

    @Bean
    open fun tracingRequestInterceptor(): FeignTracingRequestInterceptor =
        FeignTracingRequestInterceptor(clientId, tracingContext())
}
