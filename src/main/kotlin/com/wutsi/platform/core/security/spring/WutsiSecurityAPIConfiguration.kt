package com.wutsi.platform.core.security.spring

import com.fasterxml.jackson.databind.ObjectMapper
import com.wutsi.platform.core.tracing.feign.FeignTracingRequestInterceptor
import com.wutsi.platform.security.WutsiSecurityApi
import com.wutsi.platform.security.WutsiSecurityApiBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.core.env.Profiles

@Configuration
open class WutsiSecurityAPIConfiguration(
    private val env: Environment,
    private val mapper: ObjectMapper,
    private val tracingRequestInterceptor: FeignTracingRequestInterceptor
) {
    @Bean
    open fun securityApi(): WutsiSecurityApi =
        WutsiSecurityApiBuilder()
            .build(
                env = securityEnvironment(),
                mapper = mapper,
                interceptors = listOf(tracingRequestInterceptor)
            )

    private fun securityEnvironment(): com.wutsi.platform.security.Environment =
        if (env.acceptsProfiles(Profiles.of("prod")))
            com.wutsi.platform.security.Environment.PRODUCTION
        else
            com.wutsi.platform.security.Environment.SANDBOX
}
