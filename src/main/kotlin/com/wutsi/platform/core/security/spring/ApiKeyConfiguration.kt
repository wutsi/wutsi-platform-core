package com.wutsi.platform.core.security.spring

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Scheduled
import javax.annotation.PostConstruct

@Configuration
@ConditionalOnProperty(
    value = ["wutsi.platform.security.api-key"]
)
open class ApiKeyConfiguration(
    private val authenticator: ApiKeyAuthenticator,
    private val applicationTokenProvider: ApplicationTokenProvider,
    @Value("\${wutsi.platform.security.api-key}") private val apiKey: String,
) {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(ApiKeyConfiguration::class.java)
    }

    @PostConstruct
    open fun init() {
        LOGGER.info("Authenticating the Application")
        authenticate()
    }

    /**
     * Refresh the token every day
     */
    @Scheduled(cron = "0 0 * * * *")
    fun refresh() {
        LOGGER.info("Refreshing the token")
        authenticate()
    }

    private fun authenticate() {
        applicationTokenProvider.value = authenticator.authenticate(apiKey)
    }
}
