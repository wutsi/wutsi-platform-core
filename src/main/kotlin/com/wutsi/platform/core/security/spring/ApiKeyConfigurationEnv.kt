package com.wutsi.platform.core.security.spring

import com.wutsi.platform.core.security.ApiKeyProvider
import com.wutsi.platform.core.security.spring.wutsi.AbstractApiKeyConfiguration
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnProperty(
    value = ["wutsi.platform.security.api-key-provider"],
    havingValue = "env"
)
open class ApiKeyConfigurationEnv(
    @Value("\${wutsi.platform.security.api-key}") private val apiKey: String
) : AbstractApiKeyConfiguration() {
    @Bean
    override fun apiKeyProvider(): ApiKeyProvider =
        EnvApiKeyProvider(apiKey)
}
