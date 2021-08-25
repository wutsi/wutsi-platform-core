package com.wutsi.platform.core.test

import com.wutsi.platform.core.tracing.TracingContext
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse

internal class TestTracingRequestInterceptor(
    private val traceId: String = "0000-0000-0000-0000",
    private val deviceId: String = "test",
    private val clientId: String = "test"
) : ClientHttpRequestInterceptor {

    override fun intercept(request: HttpRequest, body: ByteArray, exec: ClientHttpRequestExecution): ClientHttpResponse {
        request.headers[TracingContext.HEADER_CLIENT_ID] = listOf(clientId)
        request.headers[TracingContext.HEADER_TRACE_ID] = listOf(traceId)
        request.headers[TracingContext.HEADER_DEVICE_ID] = listOf(deviceId)

        return exec.execute(request, body)
    }
}
