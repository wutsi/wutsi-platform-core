package com.wutsi.platform.core.stream

interface EventStream {
    fun close()
    fun enqueue(type: String, payload: Any)
    fun publish(type: String, payload: Any)
    fun subscribeTo(source: String)
}
