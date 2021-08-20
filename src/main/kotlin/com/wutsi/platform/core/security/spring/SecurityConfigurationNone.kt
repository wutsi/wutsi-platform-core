package com.wutsi.platform.core.security.spring

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@EnableWebSecurity
@Configuration
@ConditionalOnProperty(
    value = ["wutsi.platform.security.type"],
    havingValue = "none"
)
open class SecurityConfigurationNone : WebSecurityConfigurerAdapter() {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(SecurityConfigurationNone::class.java)
    }

    public override fun configure(http: HttpSecurity) {
        LOGGER.info("Configuring security")
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
