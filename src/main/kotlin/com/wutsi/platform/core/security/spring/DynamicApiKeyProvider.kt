package com.wutsi.platform.core.security.spring

import com.wutsi.platform.core.security.ApiKeyProvider
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import org.springframework.web.context.request.RequestContextHolder

class DynamicApiKeyProvider(
    private val context: ApplicationContext
) : ApiKeyProvider {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(DynamicApiKeyProvider::class.java)
    }

    override fun getApiKey(): String? {
        val delegate = get()
        if (delegate == null) {
            LOGGER.debug("No ApiKeyProvider available in Application Context")
            return null
        } else {
            return delegate.getApiKey()
        }
    }

    private fun get(): ApiKeyProvider? =
        if (RequestContextHolder.getRequestAttributes() != null)
            context.getBean(RequestApiKeyProvider::class.java)
        else
            null
}
