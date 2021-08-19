package com.wutsi.platform.core.security.spring.jwt

import com.auth0.jwt.JWT
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

class JWTAuthentication(val jwt: String) : Authentication {
    private var authenticated: Boolean = false
    private val decodedJWT = JWT.decode(jwt)
    private val authorities: MutableCollection<SimpleGrantedAuthority> = decodedJWT.claims["scope"]
        ?.asList(String::class.java)
        ?.map { SimpleGrantedAuthority(it) }
        ?.toMutableList()
        ?: mutableListOf()

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
}
