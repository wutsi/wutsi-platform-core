package com.wutsi.platform.core.tracing.servlet

import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.wutsi.platform.core.tracing.DeviceIdProvider
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

internal class DeviceIdProviderCookieTest {
    companion object {
        const val COOKIE_NAME: String = "_cookie"
    }

    private lateinit var request: HttpServletRequest
    private lateinit var response: HttpServletResponse
    private lateinit var provider: DeviceIdProvider

    @BeforeEach
    fun setUp() {
        request = mock()
        response = mock()
        provider = DeviceIdProviderCookie(COOKIE_NAME)
    }

    @Test
    fun `return null when cookie and attribute are null`() {
        doReturn(null).whenever(request).cookies
        doReturn(null).whenever(request).getAttribute(COOKIE_NAME)

        val value = provider.get(request)
        assertNull(value)
    }

    @Test
    fun `return null when cookie not found and attribute is null`() {
        doReturn(
            arrayOf(
                Cookie("foo1", "bar1"),
                Cookie("foo2", "bar2"),
                Cookie("foo3", "bar3")
            )
        ).whenever(request).cookies
        doReturn(null).whenever(request).getAttribute(COOKIE_NAME)

        val value = provider.get(request)

        assertNull(value)
    }

    @Test
    fun `return value from attribute when cookie not found`() {
        doReturn(null).whenever(request).cookies
        doReturn("foo").whenever(request).getAttribute(COOKIE_NAME)

        val value = provider.get(request)

        assertEquals("foo", value)
    }

    @Test
    fun set() {
        provider.set("xxx", request, response)

        verify(request).setAttribute(COOKIE_NAME, "xxx")

        val cookie = argumentCaptor<Cookie>()
        verify(response).addCookie(cookie.capture())
        assertEquals(COOKIE_NAME, cookie.firstValue.name)
        assertEquals("xxx", cookie.firstValue.value)
    }
}
