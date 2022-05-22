package com.wutsi.platform.core.security.spring

interface ApiKeyAuthenticator {
    fun authenticate(apiKey: String): String
}
