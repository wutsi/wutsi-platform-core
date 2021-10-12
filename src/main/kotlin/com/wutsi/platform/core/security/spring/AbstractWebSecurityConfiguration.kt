package com.wutsi.platform.core.security.spring

import com.wutsi.platform.core.security.servlet.CorsFilter
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

open class AbstractWebSecurityConfiguration : WebSecurityConfigurerAdapter() {
    @Bean
    open fun corsFilter() = CorsFilter()
}
