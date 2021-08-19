package com.wutsi.platform.core.logging

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.slf4j.Logger
import java.io.IOException
import java.util.Optional
import kotlin.test.assertEquals

class DefaultKVLoggerTest {
    private lateinit var logger: Logger
    private lateinit var defaultKv: DefaultKVLogger

    @BeforeEach
    fun setUp() {
        logger = mock(Logger::class.java)
        defaultKv = DefaultKVLogger(logger, LoggerEncoder())
    }

    @Test
    fun shouldLog() {
        // Given
        defaultKv.add("foo", "bar")
        defaultKv.add("john", "doe")
        defaultKv.add("valueLong", 1L)
        defaultKv.add("valueInt", 2)
        defaultKv.add("valueDouble", 3.5)
        defaultKv.add("valueOpt", Optional.of(1))
        defaultKv.add("valueCollection", listOf(1, 2))

        // When
        defaultKv.log()

        // Then
        verify(logger).info("foo=bar john=doe valueCollection=\"1 2\" valueDouble=3.5 valueInt=2 valueLong=1 valueOpt=1")
    }

    @Test
    fun shouldNotLogWhenEmpty() {
        // When
        defaultKv.log()

        // Then
        verify(logger, never()).info(anyString())
    }

    @Test
    fun shouldLogMultiValue() {
        // Given
        defaultKv.add("foo", "john")
        defaultKv.add("foo", "doe")

        // When
        defaultKv.log()

        // Then
        verify(logger).info("foo=\"john doe\"")
    }

    @Test
    fun shouldLogWithSortedKeys() {
        // Given
        defaultKv.add("Z", "bar")
        defaultKv.add("A", "doe")

        // When
        defaultKv.log()

        // Then
        verify(logger).info("A=doe Z=bar")
    }

    @Test
    fun shouldLogException() {
        // Given
        val ex = IOException("error")

        // When
        defaultKv.log(ex)

        // Then
        val msg = ArgumentCaptor.forClass(String::class.java)
        val exception = ArgumentCaptor.forClass(Throwable::class.java)
        verify(logger).error(msg.capture(), exception.capture())
        assertEquals("exception=java.io.IOException exception_message=error", msg.value)
        assertEquals(ex, exception.value)
    }

    @Test
    @Throws(Exception::class)
    fun shouldLogAMaximumOf10000Characters() {
        // Given
        val ch100 = "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"
        val longString = StringBuilder()
        for (i in 0..999) {
            longString.append(ch100).append('\n')
        }

        defaultKv.add("foo", "bar")
        defaultKv.add("john", "smith")
        defaultKv.add("name", longString.toString())

        // When
        defaultKv.log()

        // Then
        val msg = ArgumentCaptor.forClass(String::class.java)
        verify(logger).info(msg.capture())
        assertEquals(DefaultKVLogger.MAX_LENGTH, msg.value.length)
    }

    @Test
    fun shouldNotLogNullValue() {
        // Given
        defaultKv.add("foo", "bar")
        defaultKv.add("john", null as String?)

        // When
        defaultKv.log()

        // Then
        verify(logger).info("foo=bar")
    }
}
