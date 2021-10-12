package com.wutsi.platform.core.security.spring

import com.wutsi.platform.core.security.ApiKeyProvider
import javax.servlet.http.HttpServletRequest

open class RequestApiKeyProvider(private val request: HttpServletRequest) : ApiKeyProvider {
    override fun getApiKey(): String? {
        return request.getHeader("X-Api-Key")
    }
}
