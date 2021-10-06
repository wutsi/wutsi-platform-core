package com.wutsi.platform.core

import com.wutsi.platform.core.cache.spring.CacheConfigurationLocal
import com.wutsi.platform.core.cache.spring.CacheConfigurationMemcached
import com.wutsi.platform.core.error.spring.ErrorConfiguration
import com.wutsi.platform.core.logging.spring.LoggingConfiguration
import com.wutsi.platform.core.security.spring.SecurityConfigurationJWT
import com.wutsi.platform.core.security.spring.SecurityConfigurationNone
import com.wutsi.platform.core.security.spring.WutsiSecurityAPIConfiguration
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
        WutsiSecurityAPIConfiguration::class
    ]
)
annotation class WutsiApplication
