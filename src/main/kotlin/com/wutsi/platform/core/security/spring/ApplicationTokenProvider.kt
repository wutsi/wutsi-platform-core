package com.wutsi.platform.core.security.spring

import com.wutsi.platform.core.security.TokenProvider
import org.slf4j.LoggerFactory

open class ApplicationTokenProvider(
    private val authenticator: ApiKeyAuthenticator,
    private val apiKey: String
) : TokenProvider {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(ApplicationTokenProvider::class.java)
    }

    private var value: String? = null

    override fun getToken(): String? {
        if (value == null) {
            LOGGER.info("Authenticating...")
            value = authenticator.authenticate(apiKey)
        }
        return value
    }
}
