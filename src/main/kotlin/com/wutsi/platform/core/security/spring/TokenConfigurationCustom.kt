package com.wutsi.platform.core.security.spring

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnProperty(
    value = ["wutsi.platform.security.token-provider-type"],
    havingValue = "custom"
)
open class TokenConfigurationCustom {
    // Nothing in this class. This is intentionnal so that no TokenProvider is created
}
