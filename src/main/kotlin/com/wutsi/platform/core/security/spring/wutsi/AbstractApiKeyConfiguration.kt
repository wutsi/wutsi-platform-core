package com.wutsi.platform.core.security.spring.wutsi

import com.wutsi.platform.core.security.ApiKeyProvider
import com.wutsi.platform.core.security.feign.FeignApiKeyRequestInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
abstract class AbstractApiKeyConfiguration {
    @Bean
    open fun apiKeyRequestInterceptor(): FeignApiKeyRequestInterceptor =
        FeignApiKeyRequestInterceptor(apiKeyProvider())

    abstract fun apiKeyProvider(): ApiKeyProvider
}
