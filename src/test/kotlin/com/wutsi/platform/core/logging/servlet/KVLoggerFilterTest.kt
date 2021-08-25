package com.wutsi.platform.core.logging.servlet

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.wutsi.platform.core.logging.KVLogger
import com.wutsi.platform.core.tracing.DeviceIdProvider
import com.wutsi.platform.core.tracing.TracingContext
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.mockito.ArgumentMatchers
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class KVLoggerFilterTest {
    private lateinit var kv: KVLogger
    private lateinit var request: HttpServletRequest
    private lateinit var response: HttpServletResponse
    private lateinit var chain: FilterChain
    private lateinit var deviceIdProvider: DeviceIdProvider

    private lateinit var filter: KVLoggerFilter

    @BeforeEach
    fun setUp() {
        kv = mock()
        request = mock()
        response = mock()
        chain = mock()
        deviceIdProvider = mock()

        filter = KVLoggerFilter(kv, deviceIdProvider)

        doReturn("/foo/bar").whenever(request).requestURI
        doReturn(201).whenever(response).status

        doReturn("client-id").whenever(request).getHeader(TracingContext.HEADER_CLIENT_ID)
        doReturn("trace-id").whenever(request).getHeader(TracingContext.HEADER_TRACE_ID)
        doReturn("device-id").whenever(deviceIdProvider).get(any())
    }

    @Test
    @Throws(Exception::class)
    fun shouldLog() {
        val value1 = arrayOf("value1.1")
        val value2 = arrayOf("value2.1", "value2.2")
        doReturn(
            mapOf(
                "param1" to value1,
                "param2" to value2
            )
        ).whenever(request).parameterMap

        // When
        filter.doFilter(request, response, chain)

        // Then
        verify(kv).add("http_endpoint", "/foo/bar")
        verify(kv).add("http_status", 201L)
        verify(kv).add("http_param_param1", value1.toList())
        verify(kv).add("http_param_param2", value2.toList())
        verify(kv).add("success", true)

        verify(kv).add("client_id", "client-id")
        verify(kv).add("device_id", "device-id")
        verify(kv).add("trace_id", "trace-id")

        verify(kv).log()
    }

    @Test
    @Throws(Exception::class)
    fun shouldLogException() {
        // Given
        val ex = IOException("Error")
        doThrow(ex).whenever(chain).doFilter(ArgumentMatchers.any(), ArgumentMatchers.any())

        try {
            // When
            filter.doFilter(request, response, chain)

            // Then
            fail("")
        } catch (e: IOException) {

            // Then
            verify(kv).add("http_endpoint", "/foo/bar")
            verify(kv).add("http_status", 500L)
            verify(kv).add("success", false)

            verify(kv).add("client_id", "client-id")
            verify(kv).add("device_id", "device-id")
            verify(kv).add("trace_id", "trace-id")
            verify(kv).setException(e)

            verify(kv).log()
        }
    }
}
