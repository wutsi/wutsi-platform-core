package com.wutsi.platform.core.security.spring

import com.wutsi.platform.core.security.TokenProvider
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import javax.servlet.http.HttpServletRequest

@Configuration
@ConditionalOnProperty(
    value = ["wutsi.platform.security.token-provider.type"],
    havingValue = "header",
    matchIfMissing = true
)
open class TokenConfigurationHeader(
    protected val context: ApplicationContext
) : AbstractTokenConfiguration() {
    @Bean
    @Primary
    override fun tokenProvider(): TokenProvider =
        DynamicTokenProvider(context)

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
    open fun requestTokenProvider(request: HttpServletRequest): RequestTokenProvider =
        RequestTokenProvider(request)
}
