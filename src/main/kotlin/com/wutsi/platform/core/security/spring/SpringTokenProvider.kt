package com.wutsi.platform.core.security.spring

import com.wutsi.platform.core.security.TokenProvider
import org.springframework.context.ApplicationContext
import javax.servlet.http.HttpServletRequest

class SpringTokenProvider(
    private val context: ApplicationContext
) : TokenProvider {
    override fun geToken(): String? {
        val request = getHttpServletRequest()
        if (request == null) {
            return null
        } else {
            val value = request.getHeader("Authorization") ?: return null
            return if (value.startsWith("Bearer ", ignoreCase = true))
                value.substring(7)
            else
                null
        }
    }

    private fun getHttpServletRequest(): HttpServletRequest? =
        try {
            context.getBean(HttpServletRequest::class.java)
        } catch (ex: Exception) {
            null
        }
}
