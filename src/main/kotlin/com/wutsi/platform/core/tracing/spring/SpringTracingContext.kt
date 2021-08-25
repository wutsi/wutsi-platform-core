package com.wutsi.platform.core.tracing.spring

import com.wutsi.platform.core.tracing.DeviceIdProvider
import com.wutsi.platform.core.tracing.TracingContext
import com.wutsi.platform.core.tracing.servlet.HttpTracingContext
import org.springframework.context.ApplicationContext
import java.util.UUID
import javax.servlet.http.HttpServletRequest
import kotlin.concurrent.getOrSet

open class SpringTracingContext(
    private val context: ApplicationContext,
    private val deviceIdProvider: DeviceIdProvider
) : TracingContext {
    private val defaultRequestId: ThreadLocal<String> = ThreadLocal()
    private val delegate = HttpTracingContext()

    override fun traceId(): String {
        val request = getHttpServletRequest()
        if (request != null) {
            val value = delegate.traceId(request)
            if (value != null)
                return value
        }
        return defaultRequestId.getOrSet { UUID.randomUUID().toString() }
    }

    override fun clientId() = getHttpServletRequest()?.let { delegate.clientId(it) } ?: TracingContext.NONE
    override fun deviceId() = getHttpServletRequest()?.let { delegate.deviceId(it, deviceIdProvider) } ?: TracingContext.NONE

    private fun getHttpServletRequest(): HttpServletRequest? {
        try {
            return context.getBean(HttpServletRequest::class.java)
        } catch (ex: Exception) {
            return null
        }
    }
}
