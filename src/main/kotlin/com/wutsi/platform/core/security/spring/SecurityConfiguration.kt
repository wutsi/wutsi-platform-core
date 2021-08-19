package com.wutsi.platform.core.security.spring

import com.wutsi.platform.core.security.KeyProvider
import com.wutsi.platform.core.security.TokenProvider
import com.wutsi.platform.core.security.spring.jwt.JWTAuthenticationFilter
import com.wutsi.platform.core.security.spring.jwt.JWTAuthenticationProvider
import com.wutsi.platform.core.security.spring.wutsi.WutsiKeyProvider
import com.wutsi.platform.core.security.spring.wutsi.WutsiTokenProvider
import com.wutsi.platform.security.WutsiSecurityApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.OrRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher
import javax.servlet.Filter

@Configuration
open class SecurityConfiguration(
    private val securityApi: WutsiSecurityApi,
    private val context: ApplicationContext,
    @Value("\${wutsi.platform.security.api-key}") private val apiKey: String,
    @Value("\${wutsi.platform.security.secured-endpoints}") private val securedEndpoints: Array<String>
) : WebSecurityConfigurerAdapter() {
    private val securedEndpointMatcher: RequestMatcher

    init {
        val matchers = securedEndpoints.map {
            val parts = it.split("\\s+")
            if (parts.size == 1)
                AntPathRequestMatcher(parts[0])
            else if (parts.size == 2)
                AntPathRequestMatcher(parts[1], parts[0])
            else
                throw IllegalStateException("Invalid secured-endpoints value: $it")
        }
        securedEndpointMatcher = OrRequestMatcher(matchers)
    }

    public override fun configure(http: HttpSecurity) {
        if (securedEndpoints.isEmpty()) {

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
        } else {

            http
                .csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(
                    org.springframework.security.config.http.SessionCreationPolicy.STATELESS
                )
                .and()
                .authorizeRequests()
                .requestMatchers(securedEndpointMatcher).authenticated()
                .anyRequest().permitAll()
                .and()
                .addFilterBefore(
                    authenticationFilter(),
                    AnonymousAuthenticationFilter::class.java
                )
        }
    }

    public override fun configure(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(JWTAuthenticationProvider())
    }

    @Bean
    open fun keyProvider(): KeyProvider =
        WutsiKeyProvider(securityApi)

    @Bean
    open fun tokenProvider(): TokenProvider =
        SpringTokenProvider(context, WutsiTokenProvider(apiKey, securityApi))

    private fun authenticationFilter(): Filter {
        val filter = JWTAuthenticationFilter(
            requestMatcher = securedEndpointMatcher,
            tokenProvider = tokenProvider(),
            keyProvider = keyProvider()
        )
        filter.setAuthenticationManager(authenticationManagerBean())
        return filter
    }
}
