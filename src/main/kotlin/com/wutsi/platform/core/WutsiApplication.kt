package com.wutsi.platform.core

import com.wutsi.platform.core.cache.spring.CacheConfigurationLocal
import com.wutsi.platform.core.cache.spring.CacheConfigurationMemcached
import com.wutsi.platform.core.error.spring.ErrorConfiguration
import com.wutsi.platform.core.logging.spring.LoggingConfiguration
import com.wutsi.platform.core.security.spring.ApiKeyConfigurationEnv
import com.wutsi.platform.core.security.spring.ApiKeyConfigurationHeader
import com.wutsi.platform.core.security.spring.SecurityConfigurationJWT
import com.wutsi.platform.core.security.spring.SecurityConfigurationNone
import com.wutsi.platform.core.security.spring.TokenConfigurationCustom
import com.wutsi.platform.core.security.spring.TokenConfigurationHeader
import com.wutsi.platform.core.storage.spring.StorageConfigurationAws
import com.wutsi.platform.core.storage.spring.StorageConfigurationLocal
import com.wutsi.platform.core.stream.spring.StreamConfigurationLocal
import com.wutsi.platform.core.stream.spring.StreamConfigurationRabbitMQ
import com.wutsi.platform.core.tracing.spring.TracingConfiguration
import org.springframework.context.annotation.Import

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@Import(
    value = [
        ApiKeyConfigurationEnv::class,
        ApiKeyConfigurationHeader::class,
        ErrorConfiguration::class,
        CacheConfigurationLocal::class,
        CacheConfigurationMemcached::class,
        LoggingConfiguration::class,
        SecurityConfigurationJWT::class,
        SecurityConfigurationNone::class,
        StreamConfigurationLocal::class,
        StreamConfigurationRabbitMQ::class,
        StorageConfigurationLocal::class,
        StorageConfigurationAws::class,
        TracingConfiguration::class,
        TokenConfigurationCustom::class,
        TokenConfigurationHeader::class,
    ]
)
annotation class WutsiApplication
