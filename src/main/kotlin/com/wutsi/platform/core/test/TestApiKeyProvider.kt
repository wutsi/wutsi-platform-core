package com.wutsi.platform.core.test

import com.wutsi.platform.core.security.ApiKeyProvider

class TestApiKeyProvider(private var apiKey: String? = null) : ApiKeyProvider {
    override fun getApiKey() = apiKey
}
