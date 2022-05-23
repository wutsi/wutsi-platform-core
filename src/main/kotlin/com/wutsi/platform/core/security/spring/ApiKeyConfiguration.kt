package com.wutsi.platform.core.security.spring

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class ApiKeyConfiguration(
    private val authenticator: ApiKeyAuthenticator,
    @Value("\${wutsi.platform.security.api-key}") private val apiKey: String,
) {
    @Bean
    open fun applicationTokenProvider(): ApplicationTokenProvider =
        ApplicationTokenProvider(authenticator, apiKey)
}
