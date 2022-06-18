package com.wutsi.platform.core.url.spring

import com.fasterxml.jackson.databind.ObjectMapper
import com.wutsi.platform.core.url.BitlyUrlShortener
import com.wutsi.platform.core.url.UrlShortener
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnProperty(
    value = ["wutsi.platform.url-shortener.type"],
    havingValue = "bitly"
)
open class UrlShortenerConfigurationBitly(
    private val objectMapper: ObjectMapper,
    @Value("\${wutsi.platform.url-shortener.bitly.access-token}") private val accessToken: String
) {
    @Bean
    open fun urlShortener(): UrlShortener =
        BitlyUrlShortener(accessToken, objectMapper)
}
