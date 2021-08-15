package com.wutsi.platform.core.tracing

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import feign.RequestTemplate
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class FeignTracingRequestInterceptorTest {
    private lateinit var tc: TracingContext
    private lateinit var interceptorFeign: FeignTracingRequestInterceptor

    @BeforeEach
    fun setUp() {
        tc = mock()
        interceptorFeign = FeignTracingRequestInterceptor("foo", tc)
    }

    @Test
    fun apply() {
        doReturn("device-id").whenever(tc).deviceId()
        doReturn("trace-id").whenever(tc).traceId()

        val template = RequestTemplate()

        interceptorFeign.apply(template)

        assertTrue(template.headers()[TracingContext.HEADER_DEVICE_ID]!!.contains("device-id"))
        assertTrue(template.headers()[TracingContext.HEADER_TRACE_ID]!!.contains("trace-id"))
        assertTrue(template.headers()[TracingContext.HEADER_HEROKU_REQUEST_ID]!!.contains("trace-id"))
        assertTrue(template.headers()[TracingContext.HEADER_CLIENT_ID]!!.contains("foo"))
    }
}
