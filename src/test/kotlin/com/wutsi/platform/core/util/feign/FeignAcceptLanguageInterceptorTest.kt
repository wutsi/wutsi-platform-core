package com.wutsi.platform.core.util.feign

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import feign.RequestTemplate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders.ACCEPT_LANGUAGE
import javax.servlet.http.HttpServletRequest
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class FeignAcceptLanguageInterceptorTest {
    private lateinit var request: HttpServletRequest
    private lateinit var template: RequestTemplate
    private lateinit var interceptor: FeignAcceptLanguageInterceptor

    @BeforeEach
    fun setUp() {
        request = mock()
        template = RequestTemplate()
        interceptor = FeignAcceptLanguageInterceptor(request)
    }

    @Test
    fun forwardHeader() {
        doReturn("fr").whenever(request).getHeader(ACCEPT_LANGUAGE)

        interceptor.apply(template)

        assertTrue(template.headers()[ACCEPT_LANGUAGE]!!.contains("fr"))
    }

    @Test
    fun noHeader() {
        doReturn(null).whenever(request).getHeader(ACCEPT_LANGUAGE)

        interceptor.apply(template)

        assertNull(template.headers()[ACCEPT_LANGUAGE])
    }
}
