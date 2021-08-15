package com.wutsi.platform.core.error.spring

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.wutsi.platform.core.error.Error
import com.wutsi.platform.core.error.ErrorResponse
import com.wutsi.platform.core.error.Parameter
import com.wutsi.platform.core.error.ParameterType
import com.wutsi.platform.core.error.ParameterType.PARAMETER_TYPE_HEADER
import com.wutsi.platform.core.error.ParameterType.PARAMETER_TYPE_PAYLOAD
import com.wutsi.platform.core.error.exception.BadRequestException
import com.wutsi.platform.core.error.exception.ConflictException
import com.wutsi.platform.core.error.exception.ForbiddenException
import com.wutsi.platform.core.error.exception.NotFoundException
import com.wutsi.platform.core.error.exception.UnauthorizedException
import com.wutsi.platform.core.error.exception.WutsiException
import com.wutsi.platform.core.logging.KVLogger
import com.wutsi.platform.core.tracing.TracingContext
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.context.MessageSource
import org.springframework.core.MethodParameter
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindingResult
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingPathVariableException
import org.springframework.web.bind.MissingRequestCookieException
import org.springframework.web.bind.MissingRequestHeaderException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class RestControllerErrorHandlerTest {
    private lateinit var handler: RestControllerErrorHandler
    private lateinit var logger: KVLogger
    private lateinit var messages: MessageSource
    private lateinit var tracingContext: TracingContext
    private val traceId = UUID.randomUUID().toString()
    private val message = "Detail error message"

    @BeforeEach
    fun setUp() {
        logger = mock()

        messages = mock()
        doReturn(message).whenever(messages).getMessage(any(), any(), any())

        tracingContext = mock()
        doReturn(traceId).whenever(tracingContext).traceId()

        handler = RestControllerErrorHandler(messages, logger, tracingContext)
    }

    @Test
    fun onException() {
        val ex = RuntimeException("Unexpected error")
        val response = handler.onException(ex)

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.statusCode)
        assertEquals(RestControllerErrorHandler.ERROR_INTERNAL_ERROR, response.body.error.code)
        assertEquals(traceId, response.body.error.traceId)
        assertEquals(ex.message, response.body.error.message)
        assertNull(response.body.error.parameter)

        verifyLogger(response, ex)
    }

    @Test
    fun onHttpRequestMethodNotSupportedException() {
        val ex = HttpRequestMethodNotSupportedException("GET")
        val response = handler.onHttpRequestMethodNotSupportedException(ex)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertEquals(RestControllerErrorHandler.ERROR_METHOD_NOT_SUPPORTED, response.body.error.code)
        assertEquals(traceId, response.body.error.traceId)
        assertEquals(ex.message, response.body.error.message)
        assertNull(response.body.error.parameter)

        verifyLogger(response, ex)
    }

    @Test
    fun onNotFoundException() =
        testWutsiException(
            HttpStatus.NOT_FOUND,
            NotFoundException(
                Error(
                    code = "not_found",
                    downstreamCode = "1111",
                    downstreamMessage = "Resource not found",
                    parameter = Parameter(
                        name = "id",
                        type = PARAMETER_TYPE_PAYLOAD,
                        value = "1111"
                    )
                )
            )
        )

    @Test
    fun onConflictException() =
        testWutsiException(
            HttpStatus.CONFLICT,
            ConflictException(
                Error(
                    code = "payment_failed",
                    downstreamCode = "1111",
                    downstreamMessage = "Resource not found",
                    parameter = Parameter(
                        name = "id",
                        type = PARAMETER_TYPE_PAYLOAD,
                        value = "1111"
                    )
                )
            )
        )

    @Test
    fun onBadRequestException() =
        testWutsiException(
            HttpStatus.BAD_REQUEST,
            BadRequestException(
                Error(
                    code = "bad_request",
                    parameter = Parameter(
                        name = "id",
                        type = PARAMETER_TYPE_HEADER,
                        value = "1111"
                    )
                )
            )
        )

    @Test
    fun onForbiddenException() =
        testWutsiException(
            HttpStatus.FORBIDDEN,
            ForbiddenException(
                Error(
                    code = "permission_denied"
                )
            )
        )

    @Test
    fun onUnauthorizedException() =
        testWutsiException(
            HttpStatus.UNAUTHORIZED,
            UnauthorizedException(
                Error(
                    code = "login"
                )
            )
        )

    private fun testWutsiException(expectedStatus: HttpStatus, ex: WutsiException) {
        val error = ex.error
        val response = handler.onWutsiException(ex)

        assertEquals(expectedStatus, response.statusCode)
        assertEquals(error.code, response.body.error.code)
        assertEquals(traceId, response.body.error.traceId)
        assertEquals(message, response.body.error.message)
        assertEquals(error.parameter, response.body.error.parameter)

        verifyLogger(response, ex)
    }

    @Test
    fun onHttpMessageNotReadableException() {
        val ex = HttpMessageNotReadableException("error")

        val response = handler.onHttpMessageNotReadableException(ex)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals(RestControllerErrorHandler.ERROR_REQUEST_NOT_READABLE, response.body.error.code)
        assertEquals(traceId, response.body.error.traceId)
        assertEquals(ex.message, response.body.error.message)
        assertNull(response.body.error.parameter)

        verifyLogger(response, ex)
    }

    @Test
    fun onMissingServletRequestParameterException() {
        val ex = MissingServletRequestParameterException("foo", "string")

        val response = handler.onMissingServletRequestParameterException(ex)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals(RestControllerErrorHandler.ERROR_MISSING_PARAMETER, response.body.error.code)
        assertEquals(traceId, response.body.error.traceId)
        assertEquals(ex.message, response.body.error.message)
        assertEquals(ex.parameterName, response.body.error.parameter?.name)
        assertEquals(ParameterType.PARAMETER_TYPE_QUERY, response.body.error.parameter?.type)

        verifyLogger(response, ex)
    }

    @Test
    fun onMissingPathVariableException() {
        val param = mock<MethodParameter>()
        doReturn(String::class.java).whenever(param).nestedParameterType

        val ex = MissingPathVariableException("foo", param)

        val response = handler.onMissingPathVariableException(ex)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals(RestControllerErrorHandler.ERROR_MISSING_PARAMETER, response.body.error.code)
        assertEquals(traceId, response.body.error.traceId)
        assertEquals(ex.message, response.body.error.message)
        assertEquals(ex.variableName, response.body.error.parameter?.name)
        assertEquals(ParameterType.PARAMETER_TYPE_PATH, response.body.error.parameter?.type)

        verifyLogger(response, ex)
    }

    @Test
    fun onMissingRequestHeaderException() {
        val param = mock<MethodParameter>()
        doReturn(String::class.java).whenever(param).nestedParameterType

        val ex = MissingRequestHeaderException("foo", param)

        val response = handler.onMissingRequestHeaderException(ex)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals(RestControllerErrorHandler.ERROR_MISSING_PARAMETER, response.body.error.code)
        assertEquals(traceId, response.body.error.traceId)
        assertEquals(ex.message, response.body.error.message)
        assertEquals(ex.headerName, response.body.error.parameter?.name)
        assertEquals(ParameterType.PARAMETER_TYPE_HEADER, response.body.error.parameter?.type)

        verifyLogger(response, ex)
    }

    @Test
    fun onMissingRequestCookieException() {
        val param = mock<MethodParameter>()
        doReturn(String::class.java).whenever(param).nestedParameterType

        val ex = MissingRequestCookieException("foo", param)

        val response = handler.onMissingRequestCookieException(ex)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals(RestControllerErrorHandler.ERROR_MISSING_PARAMETER, response.body.error.code)
        assertEquals(traceId, response.body.error.traceId)
        assertEquals(ex.message, response.body.error.message)
        assertEquals(ex.cookieName, response.body.error.parameter?.name)
        assertEquals(ParameterType.PARAMETER_TYPE_COOKIE, response.body.error.parameter?.type)

        verifyLogger(response, ex)
    }

    @Test
    fun onMethodArgumentNotValidException() {
        val param = mock<MethodParameter>()
        doReturn("foo").whenever(param).parameterName
        doReturn(String::class.java.getMethod("toString")).whenever(param).executable

        val ex = MethodArgumentNotValidException(param, mock<BindingResult>())

        val response = handler.onMethodArgumentNotValidException(ex)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals(RestControllerErrorHandler.ERROR_INVALID_PARAMETER, response.body.error.code)
        assertEquals(traceId, response.body.error.traceId)
        assertEquals(ex.message, response.body.error.message)
        assertEquals("foo", response.body.error.parameter?.name)

        verifyLogger(response, ex)
    }

    @Test
    fun onMethodArgumentTypeMismatchException() {
        val param = mock<MethodParameter>()
        doReturn("foo").whenever(param).parameterName
        doReturn(String::class.java.getMethod("toString")).whenever(param).executable

        val ex = MethodArgumentTypeMismatchException("bar", Long::class.java, "foo", param, null)

        val response = handler.onMethodArgumentTypeMismatchException(ex)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals(RestControllerErrorHandler.ERROR_INVALID_PARAMETER, response.body.error.code)
        assertEquals(traceId, response.body.error.traceId)
        assertEquals(ex.message, response.body.error.message)
        assertEquals("foo", response.body.error.parameter?.name)
        assertEquals("bar", response.body.error.parameter?.value)

        verifyLogger(response, ex)
    }

    private fun verifyLogger(response: ResponseEntity<ErrorResponse>, ex: Throwable) {
        verify(logger).add("error_code", response.body.error.code)
        verify(logger).add("error_message", response.body.error.message)
        verify(logger).add("error_downstream_code", response.body.error.downstreamCode)
        verify(logger).add("error_downstream_message", response.body.error.downstreamMessage)
        verify(logger).add("error_parameter_name", response.body.error.parameter?.name)
        verify(logger).add("error_parameter_type", response.body.error.parameter?.type)
        verify(logger).add("error_parameter_value", response.body.error.parameter?.value)
        verify(logger).add("exception", ex.javaClass.name)
        verify(logger).add("exception_message", ex.message)
    }
}
