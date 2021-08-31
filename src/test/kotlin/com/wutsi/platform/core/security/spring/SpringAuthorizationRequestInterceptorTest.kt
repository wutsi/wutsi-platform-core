package com.wutsi.platform.core.security.spring

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.wutsi.platform.core.security.TokenProvider
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpResponse
import kotlin.test.assertTrue

internal class SpringAuthorizationRequestInterceptorTest {
    @Test
    fun intercept() {
        val headers = HttpHeaders()
        val request = mock<HttpRequest>()
        doReturn(headers).whenever(request).headers

        val response = mock<ClientHttpResponse>()
        val exec = mock<ClientHttpRequestExecution>()
        doReturn(response).whenever(exec).execute(any(), any())

        val tokenProvider = mock<TokenProvider>()
        doReturn("foo").whenever(tokenProvider).geToken()

        val interceptor = SpringAuthorizationRequestInterceptor(tokenProvider)

        interceptor.intercept(request, ByteArray(10), exec)

        assertTrue(headers["Authorization"]!![0]!!.startsWith("Bearer foo"))
    }
}
