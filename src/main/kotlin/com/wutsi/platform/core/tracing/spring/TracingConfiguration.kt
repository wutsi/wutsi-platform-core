package com.wutsi.platform.core.tracing.spring

import com.wutsi.platform.core.tracing.DeviceIdProvider
import com.wutsi.platform.core.tracing.FeignTracingRequestInterceptor
import com.wutsi.platform.core.tracing.TracingContext
import com.wutsi.platform.core.tracing.servlet.DeviceIdFilter
import com.wutsi.platform.core.tracing.servlet.DeviceIdProviderCookie
import com.wutsi.platform.core.tracing.servlet.DeviceIdProviderHeader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class TracingConfiguration(
    @Autowired private val context: ApplicationContext,
    @Value("\${wutsi.platform.tracing.client-id}") private val clientId: String,
    @Value("\${wutsi.platform.tracing.device-id-provider:header}") private val deviceIdProviderType: String,
    @Value("\${wutsi.platform.tracing.device-id-provider.cookie.name:_w_did}") private val cookieName: String
) {
    @Bean
    open fun tracingContext(): TracingContext =
        SpringTracingContext(context, deviceIdProvider())

    @Bean
    open fun tracingRequestInterceptor(): FeignTracingRequestInterceptor =
        FeignTracingRequestInterceptor(clientId, tracingContext())

    @Bean
    open fun deviceIdProvider(): DeviceIdProvider =
        if (deviceIdProviderType == "cookie")
            DeviceIdProviderCookie(cookieName)
        else
            DeviceIdProviderHeader()

    @Bean
    open fun deviceIdFilter(): DeviceIdFilter =
        DeviceIdFilter(deviceIdProvider())
}
