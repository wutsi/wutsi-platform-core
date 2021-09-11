package com.wutsi.platform.core.tracing.spring

import com.wutsi.platform.core.tracing.TracingContext
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class DefaultTracingContextTest {
    val tc: TracingContext = DefaultTracingContext()

    @Test
    fun traceId() {
        assertEquals(36, tc.traceId().length)
    }

    @Test
    fun clientId() {
        assertEquals(TracingContext.NONE, tc.clientId())
    }

    @Test
    fun deviceId() {
        assertEquals(TracingContext.NONE, tc.deviceId())
    }
}
