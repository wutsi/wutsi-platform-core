package com.wutsi.platform.core.security.spring

import com.wutsi.platform.core.security.ApiKeyProvider
import com.wutsi.platform.core.security.servlet.CorsFilter
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import javax.servlet.http.HttpServletRequest

open class AbstractWebSecurityConfiguration(protected val context: ApplicationContext) : WebSecurityConfigurerAdapter() {
    @Bean
    open fun corsFilter() = CorsFilter()

    @Bean
    @Primary
    open fun apiKeyProvider(): ApiKeyProvider =
        DynamicApiKeyProvider(context)

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
    open fun requestApiKeyProvider(request: HttpServletRequest): RequestApiKeyProvider =
        RequestApiKeyProvider(request)
}
