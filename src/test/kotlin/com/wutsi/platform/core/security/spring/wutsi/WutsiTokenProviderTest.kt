package com.wutsi.platform.core.security.spring.wutsi

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.wutsi.platform.security.WutsiSecurityApi
import com.wutsi.platform.security.dto.LoginResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class WutsiTokenProviderTest {
    private val apiKey: String = "123"
    private lateinit var securityApi: WutsiSecurityApi
    private lateinit var provider: WutsiTokenProvider

    @BeforeEach
    fun setUp() {
        securityApi = mock()
        provider = WutsiTokenProvider(apiKey, securityApi)
    }

    @Test
    fun `get token from server`() {
        val response = LoginResponse(
            accessToken = "xx.xxx.xxxx"
        )
        doReturn(response).whenever(securityApi).login(any())

        val result = provider.geToken()
        assertEquals(response.accessToken, result)
        assertEquals(response.accessToken, provider.accessToken)
    }

    @Test
    fun `get cached token`() {
        provider = WutsiTokenProvider(apiKey, securityApi, "xxx-xxx")

        val result = provider.geToken()
        assertEquals("xxx-xxx", result)
    }
}
