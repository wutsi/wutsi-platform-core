package com.wutsi.platform.core.security.spring.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import org.junit.jupiter.api.Test
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.Date
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class JWTAuthenticationTest {
    @Test
    fun test() {
        val auth = JWTAuthentication.of(createAccessToken())
        auth.setAuthenticated(true)

        assertEquals("subject", auth.name)
        assertEquals(mutableListOf(SimpleGrantedAuthority("read"), SimpleGrantedAuthority("write")), auth.authorities)
        assertTrue(auth.isAuthenticated)
        assertEquals(auth, auth.principal)
        assertTrue(auth.details is DecodedJWT)
    }

    private fun createAccessToken(): String {
        val now = System.currentTimeMillis()
        return JWT.create()
            .withIssuer("Test")
            .withIssuedAt(Date(now))
            .withExpiresAt(Date(now + 100000))
            .withJWTId("111")
            .withSubject("subject")
            .withClaim("scope", listOf("read", "write"))
            .sign(Algorithm.HMAC256("secret"))
    }
}
