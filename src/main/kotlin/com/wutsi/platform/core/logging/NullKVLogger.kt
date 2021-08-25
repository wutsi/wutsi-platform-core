package com.wutsi.platform.core.logging

import java.util.Optional

open class NullKVLogger : KVLogger {
    override fun log() {
    }

    override fun add(key: String, value: String?) {
    }

    override fun add(key: String, value: Long?) {
    }

    override fun add(key: String, value: Double?) {
    }

    override fun add(key: String, value: Optional<Any>) {
    }

    override fun add(key: String, values: Collection<Any>?) {
    }

    override fun add(key: String, value: Any?) {
    }

    override fun setException(ex: Throwable) {
    }
}
