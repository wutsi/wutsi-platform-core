package com.wutsi.platform.core.test

import com.wutsi.platform.core.security.TokenProvider

class TestTokenProvider(private var token: String) : TokenProvider {
    override fun getToken(): String =
        token
}
