package com.wutsi.platform.core.security.spring.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.wutsi.platform.core.security.KeyProvider
import com.wutsi.platform.core.security.spring.AnonymousAuthentication
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.RequestMatcher
import java.security.interfaces.RSAPublicKey
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTAuthenticationFilter(
    private val keyProvider: KeyProvider,
    requestMatcher: RequestMatcher
) : AbstractAuthenticationProcessingFilter(requestMatcher) {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(JWTAuthenticationFilter::class.java)
    }

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication? {
        val token = getToken(request)
        LOGGER.debug("Token in request header: $token")
        val auth = if (token != null) {
            validate(token)
            JWTAuthentication.of(token)
        } else {
            LOGGER.debug("No token found in the header")
            AnonymousAuthentication()
        }

        return authenticationManager.authenticate(auth)
    }

    override fun successfulAuthentication(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain, authentication: Authentication) {
        SecurityContextHolder.getContext().authentication = authentication
        chain.doFilter(request, response)
    }

    private fun validate(jwt: String) {
        try {
            val algorithm = getAlgorithm(jwt)
            JWT.require(algorithm)
                .build()
                .verify(jwt)
        } catch (ex: JWTVerificationException) {
            throw BadCredentialsException("Invalid token: $jwt", ex)
        }
    }

    private fun getAlgorithm(jwt: String): Algorithm {
        val token = JWT.decode(jwt)
        if (token.algorithm == "RS256") {
            val key = keyProvider.getKey(token.keyId)
            return Algorithm.RSA256(key as RSAPublicKey, null)
        }

        throw BadCredentialsException("Encryption algorithm not supported: ${token.algorithm}")
    }

    private fun getToken(request: HttpServletRequest): String? {
        val value = request.getHeader("Authorization") ?: return null
        return if (value.startsWith("Bearer ", ignoreCase = true))
            value.substring(7)
        else
            null
    }
}
