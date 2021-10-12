package com.wutsi.platform.core.security.feign

import com.wutsi.platform.core.security.ApiKeyProvider
import feign.RequestInterceptor
import feign.RequestTemplate

class FeignApiKeyRequestInterceptor(
    private val apiKeyProvider: ApiKeyProvider
) : RequestInterceptor {
    override fun apply(request: RequestTemplate) {
        val apiKey = apiKeyProvider.getApiKey()
        if (apiKey != null) {
            request.header("X-Api-Key", apiKey)
        }
    }
}
