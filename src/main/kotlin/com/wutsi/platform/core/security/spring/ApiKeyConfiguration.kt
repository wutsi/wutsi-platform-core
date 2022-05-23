package com.wutsi.platform.core.security.spring

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnProperty(
    value = ["wutsi.platform.security.api-key"]
)
open class ApiKeyConfiguration(
    private val authenticator: ApiKeyAuthenticator,
    @Value("\${wutsi.platform.security.api-key}") private val apiKey: String,
) {
    @Bean
    open fun applicationTokenProvider(): ApplicationTokenProvider =
        ApplicationTokenProvider(authenticator, apiKey)
}
