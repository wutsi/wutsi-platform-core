package com.wutsi.platform.core.security.spring

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.wutsi.platform.core.security.TokenProvider
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.context.ApplicationContext
import javax.servlet.http.HttpServletRequest
import kotlin.test.assertNull

internal class SpringTokenProviderTest {
    private lateinit var context: ApplicationContext
    private lateinit var request: HttpServletRequest
    private lateinit var provider: TokenProvider

    @BeforeEach
    fun setUp() {
        context = mock()
        request = mock()
        provider = SpringTokenProvider(context)
    }

    @Test
    fun `return the JWT from request header`() {
        doReturn(request).whenever(context).getBean(HttpServletRequest::class.java)
        doReturn("BeaReR 111").whenever(request).getHeader("Authorization")

        assertEquals("111", provider.geToken())
    }

    @Test
    fun `return null when request header malformed`() {
        doReturn(request).whenever(context).getBean(HttpServletRequest::class.java)
        doReturn("-- 111").whenever(request).getHeader("Authorization")

        assertNull(provider.geToken())
    }

    @Test
    fun `return null when request header not available`() {
        doReturn(request).whenever(context).getBean(HttpServletRequest::class.java)
        doReturn(null).whenever(request).getHeader("Authorization")

        assertNull(provider.geToken())
    }
}
