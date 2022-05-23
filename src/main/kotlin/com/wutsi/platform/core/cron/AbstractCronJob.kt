package com.wutsi.platform.core.cron

import com.wutsi.platform.core.logging.DefaultKVLogger
import com.wutsi.platform.core.logging.ThreadLocalKVLoggerHolder
import com.wutsi.platform.core.security.spring.ApplicationTokenProvider
import com.wutsi.platform.core.security.spring.ThreadLocalTokenProviderHolder
import com.wutsi.platform.core.tracing.DefaultTracingContext
import com.wutsi.platform.core.tracing.ThreadLocalTracingContextHolder
import org.springframework.beans.factory.annotation.Autowired
import java.util.UUID

abstract class AbstractCronJob : CronJob {
    @Autowired
    protected lateinit var tokenProvider: ApplicationTokenProvider

    abstract fun doRun(): Long

    abstract fun getJobName(): String

    override fun run() {
        // Add Logger into the ThreadLocal
        val logger = DefaultKVLogger()
        ThreadLocalKVLoggerHolder.set(logger)
        logger.add("job", getJobName())

        // Add TracingContext into the ThreadLocal
        val tc = DefaultTracingContext(
            clientId = getJobName(),
            traceId = UUID.randomUUID().toString(),
            deviceId = getJobName(),
            tenantId = null,
            clientInfo = getJobName()
        )
        ThreadLocalTracingContextHolder.set(tc)
        logger.add("client_id", tc.clientId())
        logger.add("client_info", tc.clientInfo())
        logger.add("trace_id", tc.traceId())
        logger.add("device_id", tc.deviceId())
        logger.add("tenant_id", tc.tenantId())

        // Add TokenProvider into the ThreadLocale
        ThreadLocalTokenProviderHolder.set(tokenProvider)
        try {
            val result = doRun()

            logger.add("job_result", result)
            logger.add("success", true)
        } catch (ex: Exception) {
            logger.setException(ex)
            logger.add("success", false)
        } finally {
            logger.log()

            // Clear the context
            ThreadLocalTracingContextHolder.remove()
            ThreadLocalKVLoggerHolder.remove()
            ThreadLocalTokenProviderHolder.remove()
        }
    }
}
