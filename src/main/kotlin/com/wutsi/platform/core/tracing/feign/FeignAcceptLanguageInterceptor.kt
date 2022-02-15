package com.wutsi.platform.core.tracing.feign

import feign.RequestInterceptor
import feign.RequestTemplate
import org.springframework.context.ApplicationContext
import org.springframework.http.HttpHeaders
import org.springframework.web.context.request.RequestContextHolder
import javax.servlet.http.HttpServletRequest

/**
 * Forward `Accept-Language` to downstream
 */
class FeignAcceptLanguageInterceptor(
    private val context: ApplicationContext
) : RequestInterceptor {
    override fun apply(template: RequestTemplate) {
        val language = get()?.getHeader(HttpHeaders.ACCEPT_LANGUAGE)
        if (language != null)
            template.header(HttpHeaders.ACCEPT_LANGUAGE, language)
    }

    private fun get(): HttpServletRequest? =
        if (RequestContextHolder.getRequestAttributes() != null)
            context.getBean(HttpServletRequest::class.java)
        else
            null
}
