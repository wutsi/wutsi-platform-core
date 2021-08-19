package com.wutsi.platform.core.security.spring

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
@ConditionalOnProperty(
    value = ["wutsi.platform.security.type"],
    havingValue = "none"
)
open class SecurityConfigurationNone : WebSecurityConfigurerAdapter() {
    public override fun configure(http: HttpSecurity) {
        http
            .csrf()
            .disable()
            .sessionManagement()
            .sessionCreationPolicy(
                org.springframework.security.config.http.SessionCreationPolicy.STATELESS
            )
            .and()
            .authorizeRequests()
            .anyRequest().permitAll()
    }
}