package com.wutsi.platform.core.security.spring

import com.wutsi.platform.core.security.TokenProvider
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import org.springframework.web.context.request.RequestContextHolder

class DynamicTokenProvider(
    private val context: ApplicationContext,
    private val applicationTokenProvider: ApplicationTokenProvider
) : TokenProvider {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(DynamicTokenProvider::class.java)
    }

    override fun getToken(): String? {
        val delegate = get()
        if (delegate == null) {
            LOGGER.debug("No TokenProvider available in Application Context")
            return null
        } else {
            return delegate.getToken()
        }
    }

    private fun get(): TokenProvider? =
        if (RequestContextHolder.getRequestAttributes() != null)
            context.getBean(RequestTokenProvider::class.java)
        else
            applicationTokenProvider
}
