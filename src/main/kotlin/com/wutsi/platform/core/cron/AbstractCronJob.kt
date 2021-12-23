package com.wutsi.platform.core.cron

import com.wutsi.platform.core.logging.DefaultKVLogger
import com.wutsi.platform.core.logging.ThreadLocalKVLoggerHolder
import com.wutsi.platform.core.tracing.DefaultTracingContext
import com.wutsi.platform.core.tracing.ThreadLocalTracingContextHolder
import java.util.UUID

abstract class AbstractCronJob : CronJob {
    abstract fun doRun()

    abstract fun getJobName(): String

    override fun run() {
        val logger = DefaultKVLogger()
        ThreadLocalKVLoggerHolder.set(logger)
        logger.add("job", getJobName())

        val tc = DefaultTracingContext(
            clientId = getJobName(),
            traceId = UUID.randomUUID().toString(),
            deviceId = getJobName(),
            tenantId = null
        )
        ThreadLocalTracingContextHolder.set(tc)
        logger.add("client_id", tc.clientId())
        logger.add("trace_id", tc.traceId())
        logger.add("device_id", tc.deviceId())
        logger.add("tenant_id", tc.tenantId())

        try {
            doRun()
        } catch (ex: Exception) {
            logger.setException(ex)
            logger.add("success", false)
        } finally {
            ThreadLocalTracingContextHolder.remove()
            ThreadLocalKVLoggerHolder.remove()
        }
    }
}
