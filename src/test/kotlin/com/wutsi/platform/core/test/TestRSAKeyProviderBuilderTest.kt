package com.wutsi.platform.security.test

import com.wutsi.platform.core.test.TestRSAKeyProvider
import com.wutsi.platform.core.test.TestRSAKeyProviderBuilder
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class TestRSAKeyProviderBuilderTest {
    @Test
    fun build() {
        val provider = TestRSAKeyProviderBuilder().build()
        assertTrue(provider is TestRSAKeyProvider)
    }
}
