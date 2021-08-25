package com.wutsi.platform.core.test

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.wutsi.platform.core.tracing.TracingContext
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpResponse

internal class TestTracingRequestInterceptorTest {
    @Test
    fun intercept() {
        val headers = HttpHeaders()
        val request = mock<HttpRequest>()
        doReturn(headers).whenever(request).headers

        val response = mock<ClientHttpResponse>()
        val exec = mock<ClientHttpRequestExecution>()
        doReturn(response).whenever(exec).execute(any(), any())

        val interceptor = TestTracingRequestInterceptor(
            traceId = "trace-id",
            clientId = "client-id",
            deviceId = "device-id"
        )

        interceptor.intercept(request, ByteArray(10), exec)

        assertEquals("trace-id", headers[TracingContext.HEADER_TRACE_ID]!![0])
        assertEquals("client-id", headers[TracingContext.HEADER_CLIENT_ID]!![0])
        assertEquals("device-id", headers[TracingContext.HEADER_DEVICE_ID]!![0])
    }
}
