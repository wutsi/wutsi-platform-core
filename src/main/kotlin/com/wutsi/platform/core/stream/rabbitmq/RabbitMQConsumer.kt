package com.wutsi.platform.core.stream.rabbitmq

import com.fasterxml.jackson.databind.ObjectMapper
import com.rabbitmq.client.AMQP.BasicProperties
import com.rabbitmq.client.Channel
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import com.wutsi.platform.core.logging.DefaultKVLogger
import com.wutsi.platform.core.logging.ThreadLocalKVLoggerHolder
import com.wutsi.platform.core.stream.Event
import com.wutsi.platform.core.stream.EventHandler
import org.slf4j.LoggerFactory

internal class RabbitMQConsumer(
    private val handler: EventHandler,
    private val mapper: ObjectMapper,
    channel: Channel
) : DefaultConsumer(channel) {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(RabbitMQConsumer::class.java)
    }

    override fun handleDelivery(
        consumerTag: String,
        envelope: Envelope,
        properties: BasicProperties,
        body: ByteArray
    ) {
        LOGGER.info("handleDelivery($consumerTag, $envelope,$properties...)")

        // Set the logger
        val logger = DefaultKVLogger()
        ThreadLocalKVLoggerHolder.set(logger)

        logger.add("rabbitmq_consumer_tag", consumerTag)
        try {

            val event = mapper.readValue(body, Event::class.java)
            handler.onEvent(event)
            channel.basicAck(envelope.deliveryTag, false)

            logger.add("success", true)
        } catch (ex: Exception) {
            logger.setException(ex)
            logger.add("success", false)

            channel.basicReject(
                envelope.deliveryTag,
                false /* do not requeue - message will go to DLQ */
            )
        } finally {
            logger.log()
        }
    }
}
