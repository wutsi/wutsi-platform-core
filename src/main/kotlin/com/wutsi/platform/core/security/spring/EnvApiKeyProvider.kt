package com.wutsi.platform.core.security.spring

import com.wutsi.platform.core.security.ApiKeyProvider

open class EnvApiKeyProvider(private val apiKey: String) : ApiKeyProvider {
    override fun getApiKey() = apiKey
}
