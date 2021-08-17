package com.wutsi.platform.core.error.spring

import com.wutsi.platform.core.error.Error
import com.wutsi.platform.core.error.ErrorResponse
import com.wutsi.platform.core.error.Parameter
import com.wutsi.platform.core.error.ParameterType.PARAMETER_TYPE_COOKIE
import com.wutsi.platform.core.error.ParameterType.PARAMETER_TYPE_HEADER
import com.wutsi.platform.core.error.ParameterType.PARAMETER_TYPE_PATH
import com.wutsi.platform.core.error.ParameterType.PARAMETER_TYPE_QUERY
import com.wutsi.platform.core.error.exception.WutsiException
import com.wutsi.platform.core.logging.KVLogger
import com.wutsi.platform.core.tracing.TracingContext
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingPathVariableException
import org.springframework.web.bind.MissingRequestCookieException
import org.springframework.web.bind.MissingRequestHeaderException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import java.util.Locale

@RestControllerAdvice
class RestControllerErrorHandler(
    private val messages: MessageSource,
    private val logger: KVLogger,
    private val tracingContext: TracingContext
) {
    companion object {
        const val ERROR_REQUEST_NOT_READABLE = "urn:error:wutsi:request-not-readable"
        const val ERROR_MISSING_PARAMETER = "urn:error:wutsi:missing-parameter"
        const val ERROR_INVALID_PARAMETER = "urn:error:wutsi:invalid-parameter"
        const val ERROR_INTERNAL_ERROR = "urn:error:wutsi:internal-error"
        const val ERROR_METHOD_NOT_SUPPORTED = "urn:error:wutsi:method-not-supported"
    }

    @ExceptionHandler(Throwable::class)
    fun onException(e: Throwable): ResponseEntity<ErrorResponse> =
        handleException(
            code = ERROR_INTERNAL_ERROR,
            status = INTERNAL_SERVER_ERROR,
            e = e,
            message = e.message
        )

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun onHttpRequestMethodNotSupportedException(e: HttpRequestMethodNotSupportedException): ResponseEntity<ErrorResponse> =
        handleException(
            code = ERROR_METHOD_NOT_SUPPORTED,
            status = HttpStatus.NOT_FOUND,
            e = e,
            message = e.message
        )

    @ExceptionHandler(WutsiException::class)
    fun onWutsiException(e: WutsiException): ResponseEntity<ErrorResponse> =
        handleException(
            code = e.error.code,
            status = status(e),
            e = e,
            parameter = e.error.parameter
        )

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun onHttpMessageNotReadableException(e: HttpMessageNotReadableException): ResponseEntity<ErrorResponse> =
        handleBadRequest(ERROR_REQUEST_NOT_READABLE, e)

    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun onMissingServletRequestParameterException(e: MissingServletRequestParameterException): ResponseEntity<ErrorResponse> =
        handleBadRequest(
            code = ERROR_MISSING_PARAMETER,
            e = e,
            parameter = Parameter(
                name = e.parameterName,
                type = PARAMETER_TYPE_QUERY
            )
        )

    @ExceptionHandler(MissingPathVariableException::class)
    fun onMissingPathVariableException(e: MissingPathVariableException): ResponseEntity<ErrorResponse> =
        handleBadRequest(
            ERROR_MISSING_PARAMETER,
            e,
            Parameter(
                name = e.variableName,
                type = PARAMETER_TYPE_PATH
            )
        )

    @ExceptionHandler(MissingRequestHeaderException::class)
    fun onMissingRequestHeaderException(e: MissingRequestHeaderException): ResponseEntity<ErrorResponse> =
        handleBadRequest(
            ERROR_MISSING_PARAMETER,
            e,
            Parameter(
                name = e.headerName,
                type = PARAMETER_TYPE_HEADER
            )
        )

    @ExceptionHandler(MissingRequestCookieException::class)
    fun onMissingRequestCookieException(e: MissingRequestCookieException): ResponseEntity<ErrorResponse> =
        handleBadRequest(
            ERROR_MISSING_PARAMETER,
            e,
            Parameter(
                name = e.cookieName,
                type = PARAMETER_TYPE_COOKIE
            )
        )

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun onMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> =
        handleBadRequest(
            ERROR_INVALID_PARAMETER,
            e,
            Parameter(
                name = e.parameter.parameterName ?: ""
            )
        )

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun onMethodArgumentTypeMismatchException(e: MethodArgumentTypeMismatchException): ResponseEntity<ErrorResponse> =
        handleBadRequest(
            ERROR_INVALID_PARAMETER,
            e,
            Parameter(
                name = e.parameter.parameterName ?: "",
                value = e.value
            )
        )

    private fun handleBadRequest(code: String, e: Throwable, parameter: Parameter? = null): ResponseEntity<ErrorResponse> =
        handleException(code, BAD_REQUEST, e, parameter, e.message)

    private fun handleException(
        code: String,
        status: HttpStatus,
        e: Throwable,
        parameter: Parameter? = null,
        message: String? = message(code)
    ): ResponseEntity<ErrorResponse> {
        val response = ErrorResponse(
            error = Error(
                code = code,
                message = message,
                traceId = tracingContext.traceId(),
                parameter = parameter
            )
        )

        log(response.error, e)
        return ResponseEntity
            .status(status)
            .body(response)
    }

    private fun log(error: Error, e: Throwable) {
        logger.add("error_code", error.code)
        logger.add("error_message", error.message)
        logger.add("error_downstream_code", error.downstreamCode)
        logger.add("error_downstream_message", error.downstreamMessage)
        logger.add("error_parameter_name", error.parameter?.name)
        logger.add("error_parameter_value", error.parameter?.value)
        logger.add("error_parameter_type", error.parameter?.type)

        logger.log(e)
    }

    private fun status(e: WutsiException): HttpStatus {
        val status = e::class.annotations.find { it is ResponseStatus } as ResponseStatus?
        return status?.value ?: INTERNAL_SERVER_ERROR
    }

    private fun message(code: String): String? =
        try {
            messages.getMessage(code, emptyArray(), Locale.ENGLISH)
        } catch (ex: Exception) {
            null
        }
}
