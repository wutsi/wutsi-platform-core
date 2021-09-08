package com.wutsi.platform.core.test

import com.wutsi.platform.core.tracing.TracingContext
import java.util.UUID

class TestTracingContext(
    private val clientId: String = "test",
    private val traceId: String = UUID.randomUUID().toString(),
    private val deviceId: String = "test-device"
) : TracingContext {
    override fun clientId(): String = clientId
    override fun traceId(): String = traceId
    override fun deviceId(): String = deviceId
}