package com.wutsi.platform.core.tracing.spring

import com.wutsi.platform.core.tracing.TracingContext
import org.springframework.context.ApplicationContext
import org.springframework.web.context.request.RequestContextHolder

open class DynamicTracingContext(private val context: ApplicationContext) : TracingContext {
    private val default: TracingContext = DefaultTracingContext()

    override fun traceId() = get().traceId()
    override fun clientId() = get().clientId()
    override fun deviceId() = get().deviceId()

    private fun get(): TracingContext =
        if (RequestContextHolder.getRequestAttributes() != null)
            context.getBean(RequestTracingContext::class.java)
        else
            default
}
