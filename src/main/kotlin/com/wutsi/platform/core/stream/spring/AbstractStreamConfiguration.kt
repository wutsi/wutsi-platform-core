package com.wutsi.platform.core.stream.spring

import com.wutsi.platform.core.stream.EventStream
import org.slf4j.LoggerFactory
import javax.annotation.PostConstruct

abstract class AbstractStreamConfiguration(private val subscriptions: Array<String>) {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(AbstractStreamConfiguration::class.java)
    }

    abstract fun eventStream(): EventStream

    @PostConstruct
    fun init() {
        subscriptions.forEach {
            LOGGER.info("Subscribing to $it")
            eventStream().subscribeTo(it)
        }
    }
}
