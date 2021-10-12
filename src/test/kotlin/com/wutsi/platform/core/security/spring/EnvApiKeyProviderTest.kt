package com.wutsi.platform.core.security.spring

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class EnvApiKeyProviderTest {
    @Test
    fun getApiKey() {
        val provider = EnvApiKeyProvider("foo")
        assertEquals("foo", provider.getApiKey())
    }
}
