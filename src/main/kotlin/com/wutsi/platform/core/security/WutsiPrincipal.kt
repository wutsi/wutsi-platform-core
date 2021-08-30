package com.wutsi.platform.core.security

import java.security.Principal

class WutsiPrincipal(
    val id: String,
    private val _name: String,
    val type: String,
    val admin: Boolean
) : Principal {
    override fun getName(): String =
        _name
}
