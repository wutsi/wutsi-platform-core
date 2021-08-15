package com.wutsi.platform.core.tracing.spring

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.wutsi.platform.core.tracing.TracingContext
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.context.ApplicationContext
import javax.servlet.http.HttpServletRequest

internal class SpringTracingContextTest {
    private lateinit var request: HttpServletRequest
    private lateinit var tc: TracingContext
    private lateinit var context: ApplicationContext

    @BeforeEach
    fun setUp() {
        request = mock()
        context = mock()
        tc = SpringTracingContext(context)
    }

    @Test
    fun `return trace-id from Header`() {
        doReturn(request).whenever(context).getBean(HttpServletRequest::class.java)
        doReturn("from-header").whenever(request).getHeader(TracingContext.HEADER_TRACE_ID)
        assertEquals("from-header", tc.traceId())
    }

    @Test
    fun `return trace-id from Heroku Header`() {
        doReturn(request).whenever(context).getBean(HttpServletRequest::class.java)
        doReturn("from-heroku").whenever(request).getHeader(TracingContext.HEADER_HEROKU_REQUEST_ID)
        assertEquals("from-heroku", tc.traceId())
    }

    @Test
    fun `return trace-id from ThreadLocal when request not in Bean context`() {
        doReturn(null).whenever(context).getBean(HttpServletRequest::class.java)
        assertNotNull(tc.traceId())
        assertEquals(36, tc.traceId().length)
    }

    @Test
    fun `return trace-id from ThreadLocal on error from ApplicationContext`() {
        doThrow(RuntimeException::class).whenever(context).getBean(HttpServletRequest::class.java)
        assertNotNull(tc.traceId())
        assertEquals(36, tc.traceId().length)
    }

    @Test
    fun `return trace-id from ThreadLocal when not available in request header`() {
        doReturn(request).whenever(context).getBean(HttpServletRequest::class.java)
        assertNotNull(tc.traceId())
        assertEquals(36, tc.traceId().length)
    }

    @Test
    fun `return client-id from header`() {
        doReturn(request).whenever(context).getBean(HttpServletRequest::class.java)
        doReturn("from-header").whenever(request).getHeader(TracingContext.HEADER_CLIENT_ID)
        assertEquals("from-header", tc.clientId())
    }

    @Test
    fun `return NONE as client-id when request not in Bean context`() {
        doReturn(null).whenever(context).getBean(HttpServletRequest::class.java)
        assertEquals(TracingContext.NONE, tc.clientId())
    }

    @Test
    fun `return NONE as client-id when not available in request header`() {
        doReturn(request).whenever(context).getBean(HttpServletRequest::class.java)
        assertEquals(TracingContext.NONE, tc.clientId())
    }

    @Test
    fun `return device-id from header`() {
        doReturn(request).whenever(context).getBean(HttpServletRequest::class.java)
        doReturn("from-header").whenever(request).getHeader(TracingContext.HEADER_DEVICE_ID)
        assertEquals("from-header", tc.deviceId())
    }

    @Test
    fun `return NONE as device-id when request not in Bean context`() {
        doReturn(null).whenever(context).getBean(HttpServletRequest::class.java)
        assertEquals(TracingContext.NONE, tc.deviceId())
    }

    @Test
    fun `return NONE as device-id when not available in request header`() {
        doReturn(request).whenever(context).getBean(HttpServletRequest::class.java)
        assertEquals(TracingContext.NONE, tc.deviceId())
    }
}
