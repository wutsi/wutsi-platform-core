package com.wutsi.platform.core.tracing.spring

import com.wutsi.platform.core.tracing.TracingContext
import java.util.UUID
import kotlin.concurrent.getOrSet

open class DefaultTracingContext : TracingContext {
    private val traceId: ThreadLocal<String> = ThreadLocal()

    override fun traceId() = traceId.getOrSet { UUID.randomUUID().toString() }

    override fun clientId() = TracingContext.NONE
    override fun deviceId() = TracingContext.NONE
}
