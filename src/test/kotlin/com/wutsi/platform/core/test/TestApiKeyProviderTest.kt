package com.wutsi.platform.core.test

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class TestApiKeyProviderTest {
    @Test
    fun test() {
        val provider = TestApiKeyProvider("foo")
        assertEquals("foo", provider.getApiKey())
    }
}
