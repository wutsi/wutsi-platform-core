package com.wutsi.platform.core.tracing.spring

import com.wutsi.platform.core.tracing.DeviceIdProvider
import com.wutsi.platform.core.tracing.TracingContext
import org.springframework.context.ApplicationContext
import java.util.UUID
import javax.servlet.http.HttpServletRequest
import kotlin.concurrent.getOrSet

open class SpringTracingContext(
    private val context: ApplicationContext,
    private val deviceIdProvider: DeviceIdProvider
) : TracingContext {
    private val defaultRequestId: ThreadLocal<String> = ThreadLocal()

    override fun traceId(): String {
        val request = getHttpServletRequest()
        if (request != null) {
            val header = request.getHeader(TracingContext.HEADER_TRACE_ID) ?: request.getHeader(TracingContext.HEADER_HEROKU_REQUEST_ID)
            if (header != null)
                return header
        }
        return defaultRequestId.getOrSet { UUID.randomUUID().toString() }
    }

    override fun clientId() = getHttpServletRequest()?.getHeader(TracingContext.HEADER_CLIENT_ID) ?: TracingContext.NONE
    override fun deviceId() = getHttpServletRequest()?.let { deviceIdProvider.get(it) } ?: TracingContext.NONE

    private fun getHttpServletRequest(): HttpServletRequest? {
        try {
            return context.getBean(HttpServletRequest::class.java)
        } catch (ex: Exception) {
            return null
        }
    }
}
