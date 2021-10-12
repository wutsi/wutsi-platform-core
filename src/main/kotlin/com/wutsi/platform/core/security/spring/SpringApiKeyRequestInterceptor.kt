package com.wutsi.platform.core.security.spring

import com.wutsi.platform.core.security.ApiKeyProvider
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse

class SpringApiKeyRequestInterceptor(
    private val apiKeyProvider: ApiKeyProvider
) : ClientHttpRequestInterceptor {

    override fun intercept(request: HttpRequest, body: ByteArray, exec: ClientHttpRequestExecution): ClientHttpResponse {
        val apiKey = apiKeyProvider.getApiKey()
        if (apiKey != null)
            request.headers["X-Api-Key"] = listOf(apiKey)

        return exec.execute(request, body)
    }
}
