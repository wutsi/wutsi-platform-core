package com.wutsi.platform.core.url.spring

import com.wutsi.platform.core.url.NullUrlShortener
import com.wutsi.platform.core.url.UrlShortener
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnProperty(
    value = ["wutsi.platform.url-shortener.type"],
    havingValue = "none",
    matchIfMissing = true
)
open class UrlShortenerConfigurationNone {
    @Bean
    open fun urlShortener(): UrlShortener =
        NullUrlShortener()
}
