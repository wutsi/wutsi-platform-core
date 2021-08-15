package com.wutsi.platform.core.logging.spring

import com.wutsi.platform.core.logging.DefaultKVLogger
import com.wutsi.platform.core.logging.KVLogger
import org.springframework.context.ApplicationContext
import java.util.Optional

open class SpringKVLogger(
    private val context: ApplicationContext,
    private val fallback: KVLogger
) : KVLogger {
    override fun log() {
        getLogger().log()
    }

    override fun log(ex: Throwable) {
        getLogger().log(ex)
    }

    override fun add(key: String, value: String?) {
        getLogger().add(key, value)
    }

    override fun add(key: String, value: Long?) {
        getLogger().add(key, value)
    }

    override fun add(key: String, value: Double?) {
        getLogger().add(key, value)
    }

    override fun add(key: String, value: Optional<Any>) {
        getLogger().add(key, value)
    }

    override fun add(key: String, values: Collection<Any>?) {
        getLogger().add(key, values)
    }

    override fun add(key: String, value: Any?) {
        getLogger().add(key, value)
    }

    private fun getLogger(): KVLogger {
        try {
            return context.getBean(DefaultKVLogger::class.java)
        } catch (ex: Exception) {
            return fallback
        }
    }
}
