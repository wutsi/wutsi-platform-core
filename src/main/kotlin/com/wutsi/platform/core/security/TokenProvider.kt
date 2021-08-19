package com.wutsi.platform.core.security

interface TokenProvider {
    fun geToken(): String?
}
