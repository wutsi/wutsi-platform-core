package com.wutsi.platform.core.security.spring

import com.wutsi.platform.core.security.TokenProvider

open class ApplicationTokenProvider(var value: String? = null) : TokenProvider {
    override fun getToken() = value
}
