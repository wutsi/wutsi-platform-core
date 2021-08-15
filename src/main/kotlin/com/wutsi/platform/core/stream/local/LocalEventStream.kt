package com.wutsi.platform.core.stream.local

import com.fasterxml.jackson.databind.ObjectMapper
import com.wutsi.platform.core.stream.Event
import com.wutsi.platform.core.stream.EventHandler
import com.wutsi.platform.core.stream.EventStream
import com.wutsi.platform.core.util.ObjectMapperBuilder
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.Files
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.util.Date
import java.util.UUID
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

/**
 * Implementation of {@link com.wutsi.stream.Stream} that uses file system as storage for events
 */
class LocalEventStream(
    private val name: String,
    private val root: File,
    private val handler: EventHandler,
    private val pollDelayMilliseconds: Long = 300
) : EventStream {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(LocalEventStream::class.java)
        private const val OUTPUT = "out"
        private const val INPUT = "in"
    }

    val input: File
    val output: File
    private val executor: ScheduledExecutorService = Executors.newScheduledThreadPool(1)
    private val mapper: ObjectMapper = ObjectMapperBuilder.build()

    init {
        this.input = createIntputFile(name)
        this.output = createOutputFile(name)

        watch(this.input, handler)
    }

    override fun close() {
        executor.shutdown()
    }

    override fun enqueue(type: String, payload: Any) {
        LOGGER.info("enqueue($type, $payload)")
        val event = createEvent(type, payload)
        persist(event, this.input)
    }

    override fun publish(type: String, payload: Any) {
        LOGGER.info("publish($type, $payload)")
        val event = createEvent(type, payload)
        persist(event, this.output)
    }

    override fun subscribeTo(source: String) {
        val file = createOutputFile(source)
        watch(
            file,
            object : EventHandler {
                override fun onEvent(event: Event) {
                    enqueue(event.type, event.payload)
                }
            }
        )
    }

    private fun persist(event: Event, directory: File) {
        if (!directory.exists())
            directory.mkdirs()

        val now = SimpleDateFormat("yyyyMMddHHmm").format(Date())
        val file = File(directory, "$now-${event.id}.json")
        LOGGER.info("Storing event to $file")

        Files.writeString(file.toPath(), mapper.writeValueAsString(event))
    }

    private fun watch(directory: File, handler: EventHandler) = DirectoryWatcher(
        directory = directory,
        handler = handler,
        pollDelayMilliseconds = pollDelayMilliseconds,
        executor = executor
    )

    private fun createEvent(type: String, payload: Any) = Event(
        id = UUID.randomUUID().toString(),
        type = type,
        timestamp = OffsetDateTime.now(),
        payload = mapper.writeValueAsString(payload)
    )

    private fun createIntputFile(name: String) = File(File(root, name), INPUT)

    private fun createOutputFile(name: String) = File(File(root, name), OUTPUT)
}
