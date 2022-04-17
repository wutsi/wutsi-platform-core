package com.wutsi.platform.core.image.spring

import com.wutsi.platform.core.image.ImageService
import com.wutsi.platform.core.image.none.ImageServiceNone
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnProperty(
    value = ["wutsi.platform.image.type"],
    havingValue = "none",
    matchIfMissing = true
)
open class ImageConfigurationNone {
    @Bean
    open fun imageService(): ImageService =
        ImageServiceNone()
}
