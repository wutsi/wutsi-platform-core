package com.wutsi.platform.core.security.spring.jwt

import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication

class JWTAuthenticationProvider : AuthenticationProvider {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(JWTAuthenticationProvider::class.java)
    }

    override fun authenticate(auth: Authentication): Authentication {
        LOGGER.debug("Authenticating $auth")
        auth.isAuthenticated = true
        return auth
    }

    override fun supports(clazz: Class<*>) = JWTAuthentication::class.java == clazz
}
