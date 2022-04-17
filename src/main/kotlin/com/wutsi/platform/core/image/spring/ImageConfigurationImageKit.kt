package com.wutsi.platform.core.image.spring

import com.wutsi.platform.core.image.ImageService
import com.wutsi.platform.core.image.imagekit.ImageKitService
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnProperty(
    value = ["wutsi.platform.image.type"],
    havingValue = "image-kit"
)
open class ImageConfigurationImageKit(
    @Value("\${wutsi.platform.image.image-kit.origin-url}") private val originUrl: String,
    @Value("\${wutsi.platform.image.image-kit.endpoint-url}") private val endpointUrl: String
) {
    @Bean
    open fun imageService(): ImageService =
        ImageKitService(originUrl, endpointUrl)
}
