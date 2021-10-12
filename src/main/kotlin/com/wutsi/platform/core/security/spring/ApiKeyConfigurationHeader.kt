package com.wutsi.platform.core.security.spring

import com.wutsi.platform.core.security.ApiKeyProvider
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import javax.servlet.http.HttpServletRequest

@Configuration
@ConditionalOnProperty(
    value = ["wutsi.platform.security.api-key-provider"],
    havingValue = "header",
    matchIfMissing = true
)
open class ApiKeyConfigurationHeader(
    protected val context: ApplicationContext
) : WebSecurityConfigurerAdapter() {
    @Bean
    @Primary
    open fun apiKeyProvider(): ApiKeyProvider =
        DynamicApiKeyProvider(context)

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
    open fun requestApiKeyProvider(request: HttpServletRequest): RequestApiKeyProvider =
        RequestApiKeyProvider(request)
}
