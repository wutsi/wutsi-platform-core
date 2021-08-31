package com.wutsi.platform.core.test

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class TestTracingContextTest {
    @Test
    fun test() {
        val tc = TestTracingContext("client-id", "trace-id", "device-id")

        assertEquals("client-id", tc.clientId())
        assertEquals("device-id", tc.deviceId())
        assertEquals("trace-id", tc.traceId())
    }
}
