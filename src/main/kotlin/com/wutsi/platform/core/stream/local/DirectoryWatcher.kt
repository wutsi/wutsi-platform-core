package com.wutsi.platform.core.stream.local

import com.fasterxml.jackson.databind.ObjectMapper
import com.wutsi.platform.core.logging.DefaultKVLogger
import com.wutsi.platform.core.logging.ThreadLocalKVLoggerHolder
import com.wutsi.platform.core.security.spring.ApplicationTokenProvider
import com.wutsi.platform.core.stream.Event
import com.wutsi.platform.core.stream.EventHandler
import com.wutsi.platform.core.stream.StreamLoggerHelper
import com.wutsi.platform.core.tracing.DefaultTracingContext
import com.wutsi.platform.core.tracing.ThreadLocalTracingContextHolder
import com.wutsi.platform.core.util.ObjectMapperBuilder
import java.io.File
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardWatchEventKinds
import java.nio.file.WatchEvent
import java.nio.file.WatchKey
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit.MILLISECONDS

/**
 * Watch a directory. One a file is created in the directory
 * <ul>
 *     <li>The content of the file is deserialized to an @{link com.wutsi.stream.Event}</li>
 *     </li>Then the event is send to a {link com.wutsi.stream.EventHandler} to handle it</li>
 * </ul>
 */
class DirectoryWatcher(
    private val directory: File,
    private val handler: EventHandler,
    private val applicationTokenProvider: ApplicationTokenProvider,
    private val pollDelayMilliseconds: Long = 1000,
    private val executor: ScheduledExecutorService = Executors.newScheduledThreadPool(1)
) : Runnable {
    private val key: WatchKey
    private val mapper: ObjectMapper = ObjectMapperBuilder.build()

    init {
        val watcher = FileSystems.getDefault().newWatchService()
        if (!directory.exists()) {
            directory.mkdirs()
        }
        key = directory.toPath().register(watcher, StandardWatchEventKinds.ENTRY_CREATE)
        executor.scheduleAtFixedRate(this, 0, pollDelayMilliseconds, MILLISECONDS)
    }

    override fun run() {
        val events = key.pollEvents()
        events.forEach {
            val watch = it as WatchEvent<Path>
            val path = watch.context()
            val file = File(directory, path.toFile().name)

            val logger = DefaultKVLogger()
            ThreadLocalKVLoggerHolder.set(logger)
            logger.add("stream_file", file.path)

            try {
                val json = Files.readString(file.toPath())
                val event = mapper.readValue(json, Event::class.java)
                StreamLoggerHelper.log(event, logger)

                // Setup the tracing context
                val tc = DefaultTracingContext(
                    clientId = "_stream-local_",
                    traceId = event.tracingData.traceId,
                    deviceId = event.tracingData.deviceId,
                    tenantId = event.tracingData.tenantId,
                    clientInfo = null
                )
                ThreadLocalTracingContextHolder.set(tc)
                StreamLoggerHelper.log(tc, logger)

                // Add TokenProvider into the ThreadLocal
                val token = applicationTokenProvider.getToken()
                if (token != null) {
                    logger.add("authorization", "***")
                }

                // Handle the event
                handler.onEvent(event)
                logger.add("success", true)
            } catch (ex: Exception) {
                logger.setException(ex)
                logger.add("success", false)
            } finally {
                logger.log()

                ThreadLocalKVLoggerHolder.remove()
                ThreadLocalTracingContextHolder.remove()
            }
        }
        key.reset()
    }
}
