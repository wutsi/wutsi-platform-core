package com.wutsi.platform.core.security.spring

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.wutsi.platform.core.security.ApiKeyProvider
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpResponse

internal class SpringApiKeyRequestInterceptorTest {
    private lateinit var headers: HttpHeaders
    private lateinit var request: HttpRequest
    private lateinit var response: ClientHttpResponse
    private lateinit var exec: ClientHttpRequestExecution
    private lateinit var apiKeyProvider: ApiKeyProvider
    private lateinit var interceptor: SpringApiKeyRequestInterceptor

    @BeforeEach
    fun setUp() {
        headers = HttpHeaders()
        request = mock()
        doReturn(headers).whenever(request).headers

        response = mock()
        exec = mock()
        doReturn(response).whenever(exec).execute(any(), any())

        apiKeyProvider = mock()

        interceptor = SpringApiKeyRequestInterceptor(apiKeyProvider)
    }

    @Test
    fun interceptWithApiKey() {
        doReturn("foo").whenever(apiKeyProvider).getApiKey()

        interceptor.intercept(request, ByteArray(10), exec)

        kotlin.test.assertTrue(headers["X-Api-Key"]!![0]!!.startsWith("foo"))
    }

    @Test
    fun interceptWithNoToken() {
        doReturn(null).whenever(apiKeyProvider).getApiKey()

        interceptor.intercept(request, ByteArray(10), exec)

        kotlin.test.assertNull(headers["X-Api-Key"])
    }
}
