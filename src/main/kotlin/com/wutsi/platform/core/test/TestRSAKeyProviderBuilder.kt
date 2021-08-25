package com.wutsi.platform.core.test

import com.auth0.jwt.interfaces.RSAKeyProvider

class TestRSAKeyProviderBuilder {
    fun build(): RSAKeyProvider =
        TestRSAKeyProvider()
}
