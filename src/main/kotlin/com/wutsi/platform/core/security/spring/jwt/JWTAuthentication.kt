package com.wutsi.platform.core.security.spring.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.interfaces.DecodedJWT
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

class JWTAuthentication(private val decodedJWT: DecodedJWT) : Authentication {
    private var authenticated: Boolean = false
    private val authorities: MutableCollection<SimpleGrantedAuthority> = decodedJWT.claims["scope"]
        ?.asList(String::class.java)
        ?.map { SimpleGrantedAuthority(it) }
        ?.toMutableList()
        ?: mutableListOf()

    companion object {
        fun of(jwt: String): JWTAuthentication =
            JWTAuthentication(JWT.decode(jwt))
    }

    override fun getName(): String =
        decodedJWT.subject

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        authorities

    override fun getCredentials(): Any =
        ""

    override fun getDetails(): Any =
        decodedJWT

    override fun getPrincipal(): Any =
        this

    override fun isAuthenticated(): Boolean =
        authenticated

    override fun setAuthenticated(value: Boolean) {
        this.authenticated = value
    }

    override fun toString(): String =
        "JWTAuthentication{${decodedJWT.token}}"
}
