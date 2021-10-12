package com.wutsi.platform.core.security.spring

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.servlet.http.HttpServletRequest

internal class RequestApiKeyProviderTest {
    lateinit var request: HttpServletRequest
    lateinit var provider: RequestApiKeyProvider

    @BeforeEach
    fun setUp() {
        request = mock()
        provider = RequestApiKeyProvider(request)
    }

    @Test
    fun getApiKey() {
        doReturn("xxx").whenever(request).getHeader("X-Api-Key")
        assertEquals("xxx", provider.getApiKey())
    }

    @Test
    fun noApiKey() {
        assertNull(provider.getApiKey())
    }
}
