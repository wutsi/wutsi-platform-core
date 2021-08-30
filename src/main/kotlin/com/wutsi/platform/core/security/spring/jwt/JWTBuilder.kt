package com.wutsi.platform.core.security.spring.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.RSAKeyProvider
import java.util.Date

class JWTBuilder(
    private val subject: String,
    private val subjectName: String,
    private val subjectType: String,
    private val keyProvider: RSAKeyProvider,
    private val admin: Boolean = false,
    private val ttl: Long = 84600,
    private val scope: List<String> = emptyList()
) {
    companion object {
        const val ISSUER = "wutsi.com"
        const val CLAIM_SUBJECT_TYPE = "sub_type"
        const val CLAIM_SUBJECT_NAME = "sub_name"
        const val CLAIM_SCOPE = "scope"
        const val CLAIM_ADMIN = "admin"
    }

    fun build(): String =
        JWT.create()
            .withIssuer(ISSUER)
            .withIssuedAt(Date(System.currentTimeMillis()))
            .withExpiresAt(Date(System.currentTimeMillis() + ttl))
            .withJWTId(keyProvider.privateKeyId)
            .withSubject(subject)
            .withClaim(CLAIM_SUBJECT_TYPE, subjectType)
            .withClaim(CLAIM_SUBJECT_NAME, subjectName)
            .withClaim(CLAIM_SCOPE, scope)
            .withClaim(CLAIM_ADMIN, admin)
            .sign(Algorithm.RSA256(keyProvider))
}
