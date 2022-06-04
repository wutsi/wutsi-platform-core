package com.wutsi.platform.core.security.spring

import com.wutsi.platform.core.security.TokenProvider
import org.springframework.context.ApplicationContext
import org.springframework.web.context.request.RequestContextHolder

class DynamicTokenProvider(
    private val context: ApplicationContext,
    private val applicationTokenProvider: ApplicationTokenProvider
) : TokenProvider {
    override fun getToken(): String? =
        get()?.getToken() ?: applicationTokenProvider.getToken()

    private fun get(): TokenProvider? =
        if (RequestContextHolder.getRequestAttributes() != null)
            context.getBean(RequestTokenProvider::class.java)
        else
            null
}
