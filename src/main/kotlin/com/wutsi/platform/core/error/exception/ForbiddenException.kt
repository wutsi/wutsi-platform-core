package com.wutsi.platform.core.error.exception

import com.wutsi.platform.core.error.Error
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.FORBIDDEN)
class ForbiddenException(error: Error, ex: Throwable? = null) : WutsiException(error, ex)
