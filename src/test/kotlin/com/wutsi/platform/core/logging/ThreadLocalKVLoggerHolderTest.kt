package com.wutsi.platform.core.logging

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ThreadLocalKVLoggerHolderTest {
    @BeforeEach
    fun setUp() {
        ThreadLocalKVLoggerHolder.remove()
    }

    @Test
    fun test() {
        assertNull(ThreadLocalKVLoggerHolder.get())

        val logger = DefaultKVLogger()
        ThreadLocalKVLoggerHolder.set(logger)
        assertEquals(logger, ThreadLocalKVLoggerHolder.get())

        ThreadLocalKVLoggerHolder.remove()
        assertNull(ThreadLocalKVLoggerHolder.get())
    }
}
