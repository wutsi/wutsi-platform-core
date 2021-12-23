package com.wutsi.platform.core.security.spring

import com.wutsi.platform.core.security.TokenProvider

object ThreadLocalTokenProviderHolder {
    private val value: ThreadLocal<TokenProvider?> = ThreadLocal()

    fun set(tokenProvider: TokenProvider) {
        value.set(tokenProvider)
    }

    fun get(): TokenProvider? =
        value.get()

    fun remove() {
        value.remove()
    }
}
