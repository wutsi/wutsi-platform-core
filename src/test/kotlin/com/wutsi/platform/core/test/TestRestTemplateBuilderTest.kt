package com.wutsi.platform.security.test

import com.auth0.jwt.interfaces.RSAKeyProvider
import com.nhaarman.mockitokotlin2.mock
import com.wutsi.platform.core.test.TestRestTemplateBuilder
import com.wutsi.platform.core.test.TestSecurityRequestInterceptor
import com.wutsi.platform.core.test.TestTracingRequestInterceptor
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class TestRestTemplateBuilderTest {
    @Test
    fun build() {
        val keyProvider = mock<RSAKeyProvider>()
        val rest = TestRestTemplateBuilder(keyProvider).build()
        assertEquals(2, rest.interceptors.size)
        assertTrue(rest.interceptors[0] is TestTracingRequestInterceptor)
        assertTrue(rest.interceptors[1] is TestSecurityRequestInterceptor)
    }
}
