package com.wutsi.platform.core.security

interface ApiKeyProvider {
    fun getApiKey(): String?
}
