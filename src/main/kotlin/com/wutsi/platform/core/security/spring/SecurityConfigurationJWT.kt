package com.wutsi.platform.core.security.spring

import com.wutsi.platform.core.security.KeyProvider
import com.wutsi.platform.core.security.TokenProvider
import com.wutsi.platform.core.security.spring.jwt.JWTAuthenticationFilter
import com.wutsi.platform.core.security.spring.jwt.JWTAuthenticationProvider
import com.wutsi.platform.core.security.spring.wutsi.WutsiKeyProvider
import com.wutsi.platform.security.WutsiSecurityApi
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.NegatedRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher
import javax.servlet.Filter

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
@ConditionalOnProperty(
    value = ["wutsi.platform.security.type"],
    havingValue = "jwt",
    matchIfMissing = true
)
open class SecurityConfigurationJWT(
    private val securityApi: WutsiSecurityApi,
    private val context: ApplicationContext
) : WebSecurityConfigurerAdapter() {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(SecurityConfigurationJWT::class.java)
    }

    public override fun configure(http: HttpSecurity) {
        LOGGER.info("Configuring HttpSecurity")
        http
            .csrf()
            .disable()
            .sessionManagement()
            .sessionCreationPolicy(
                org.springframework.security.config.http.SessionCreationPolicy.STATELESS
            )
            .and()
            .authorizeRequests()
            .requestMatchers(securedEndpoints()).authenticated()
            .anyRequest().permitAll()
            .and()
            .addFilterBefore(
                authenticationFilter(),
                AnonymousAuthenticationFilter::class.java
            )
    }

    public override fun configure(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(JWTAuthenticationProvider())
    }

    @Bean
    open fun keyProvider(): KeyProvider =
        WutsiKeyProvider(securityApi)

    @Bean
    open fun tokenProvider(): TokenProvider =
        SpringTokenProvider(context)

    private fun authenticationFilter(): Filter {
        val filter = JWTAuthenticationFilter(
            requestMatcher = securedEndpoints(),
            keyProvider = keyProvider()
        )
        filter.setAuthenticationManager(authenticationManagerBean())
        return filter
    }

    private fun securedEndpoints(): RequestMatcher =
        NegatedRequestMatcher(
            AntPathRequestMatcher("/actuator/*", "GET") // Actuator endpoints
        )
}
