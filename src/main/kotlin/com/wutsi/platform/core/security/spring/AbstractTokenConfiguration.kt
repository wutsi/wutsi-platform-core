package com.wutsi.platform.core.security.spring

import com.wutsi.platform.core.security.TokenProvider
import com.wutsi.platform.core.security.feign.FeignAuthorizationRequestInterceptor
import org.springframework.context.annotation.Bean

abstract class AbstractTokenConfiguration {
    abstract fun tokenProvider(): TokenProvider

    @Bean
    open fun authorizationRequestInterceptor(): FeignAuthorizationRequestInterceptor =
        FeignAuthorizationRequestInterceptor(tokenProvider())
}
