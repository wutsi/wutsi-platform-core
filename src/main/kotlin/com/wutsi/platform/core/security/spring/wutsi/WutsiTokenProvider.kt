package com.wutsi.platform.core.security.spring.wutsi

import com.wutsi.platform.core.security.TokenProvider
import com.wutsi.platform.security.WutsiSecurityApi
import com.wutsi.platform.security.dto.LoginRequest
import org.slf4j.LoggerFactory

class WutsiTokenProvider(
    private val apiKey: String,
    private val securityApi: WutsiSecurityApi
) : TokenProvider {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(WutsiTokenProvider::class.java)
    }

    private var accessToken: String? = null

    override fun geToken(): String? =
        login()

    private fun login(): String? {
        if (accessToken == null) {
            LOGGER.info("Logging in...")

            accessToken = securityApi.login(
                LoginRequest(
                    apiKey = apiKey
                )
            ).accessToken
        }
        return accessToken
    }
}
