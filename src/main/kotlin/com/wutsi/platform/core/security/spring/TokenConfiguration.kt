package com.wutsi.platform.core.security.spring

import com.wutsi.platform.core.security.TokenProvider
import com.wutsi.platform.core.security.feign.FeignAuthorizationRequestInterceptor
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import javax.servlet.http.HttpServletRequest

@Configuration
open class TokenConfiguration(protected val context: ApplicationContext) {
    @Bean
    open fun authorizationRequestInterceptor(): FeignAuthorizationRequestInterceptor =
        FeignAuthorizationRequestInterceptor(tokenProvider())

    @Bean
    open fun tokenProvider(): TokenProvider =
        DynamicTokenProvider(context)

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
    open fun requestTokenProvider(request: HttpServletRequest): RequestTokenProvider =
        RequestTokenProvider(request)
}
