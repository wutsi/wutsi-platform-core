package com.wutsi.platform.core.test

import com.auth0.jwt.interfaces.RSAKeyProvider
import org.springframework.web.client.RestTemplate

class TestRestTemplateBuilder(
    private var keyProvider: RSAKeyProvider,
    private var scopes: List<String> = emptyList()
) {
    fun build(): RestTemplate {
        val rest = RestTemplate()

        rest.interceptors.add(TestTracingRequestInterceptor())
        rest.interceptors.add(TestSecurityRequestInterceptor(keyProvider, scopes))

        return rest
    }
}
