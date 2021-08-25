package com.wutsi.platform.core.logging.spring

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.wutsi.platform.core.logging.DefaultKVLogger
import com.wutsi.platform.core.logging.KVLogger
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.context.ApplicationContext
import java.util.Optional

internal class SpringKVLoggerTest {
    lateinit var context: ApplicationContext
    lateinit var requestLogger: DefaultKVLogger
    lateinit var fallback: KVLogger
    lateinit var logger: SpringKVLogger

    @BeforeEach
    fun setUp() {
        requestLogger = mock()
        fallback = mock()

        context = mock()

        logger = SpringKVLogger(context, fallback)
    }

    @ParameterizedTest
    @MethodSource("data")
    fun `add when logger is available in the context`(key: String, value: Any?) {
        doReturn(requestLogger).whenever(context).getBean(DefaultKVLogger::class.java)

        logger.add(key, value)

        verify(requestLogger).add(key, value)
        verify(fallback, never()).add(key, value)
    }

    @Test
    fun `log when logger is available in the context`() {
        doReturn(requestLogger).whenever(context).getBean(DefaultKVLogger::class.java)

        logger.log()

        verify(requestLogger).log()
        verify(fallback, never()).log()
    }

    @ParameterizedTest
    @MethodSource("data")
    fun `add when logger is not available in the context`(key: String, value: Any?) {
        doThrow(RuntimeException::class).whenever(context).getBean(DefaultKVLogger::class.java)

        logger.add(key, value)

        verify(requestLogger, never()).add(key, value)
        verify(fallback).add(key, value)
    }

    @Test
    fun `log when logger is not available in the context`() {
        doThrow(RuntimeException::class).whenever(context).getBean(DefaultKVLogger::class.java)

        logger.log()

        verify(requestLogger, never()).log()
        verify(fallback).log()
    }

    companion object {
        @JvmStatic
        fun data() = listOf(
            Arguments.of("foo", "bar"),
            Arguments.of("foo", 1L),
            Arguments.of("foo", 1.0),
            Arguments.of("foo", null),
            Arguments.of("foo", Optional.of("bar")),
            Arguments.of("foo", listOf("1", "2"))
        )
    }
}
