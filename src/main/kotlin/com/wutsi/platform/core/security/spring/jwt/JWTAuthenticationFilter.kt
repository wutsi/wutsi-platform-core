package com.wutsi.platform.core.security.spring.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.wutsi.platform.core.security.KeyProvider
import com.wutsi.platform.core.security.TokenProvider
import com.wutsi.platform.core.security.spring.AnonymousAuthentication
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
    private val tokenProvider: TokenProvider,
    private val keyProvider: KeyProvider,
    requestMatcher: RequestMatcher
) : AbstractAuthenticationProcessingFilter(requestMatcher) {
    companion object {
        private val ANONYMOUS = AnonymousAuthentication()
    }

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication? {
        val jwt = tokenProvider.geToken() ?: return ANONYMOUS

        validate(jwt)
        return authenticationManager.authenticate(JWTAuthentication(jwt))
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
            throw BadCredentialsException("Invalid token: $jwt")
        }
    }

    private fun getAlgorithm(jwt: String): Algorithm {
        val token = JWT.decode(jwt)
        if (token.algorithm == "RSA256") {
            val key = keyProvider.getKey(token.keyId)
            return Algorithm.RSA256(key as RSAPublicKey, null)
        }
        throw BadCredentialsException("Encryption algorithm not supported: ${token.algorithm}")
    }
}
