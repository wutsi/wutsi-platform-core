package com.wutsi.platform.core.test

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.RSAKeyProvider
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import java.util.Date

internal class TestSecurityRequestInterceptor(
    private val keyProvider: RSAKeyProvider,
    private val scopes: List<String>
) : ClientHttpRequestInterceptor {

    override fun intercept(request: HttpRequest, body: ByteArray, exec: ClientHttpRequestExecution): ClientHttpResponse {
        val token = createToken()
        request.headers["Authorization"] = listOf("Bearer $token")

        return exec.execute(request, body)
    }

    private fun createToken(): String =
        JWT.create()
            .withIssuer("wutsi-test")
            .withIssuedAt(Date())
            .withExpiresAt(Date(System.currentTimeMillis() + 86400))
            .withSubject("urn:user:wutsi:test")
            .withClaim("name", "test")
            .withClaim("scope", scopes)
            .sign(Algorithm.RSA256(keyProvider))
}
