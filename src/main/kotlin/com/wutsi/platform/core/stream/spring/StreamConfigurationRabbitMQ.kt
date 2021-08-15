package com.wutsi.platform.core.stream.spring

import com.rabbitmq.client.Channel
import com.rabbitmq.client.ConnectionFactory
import com.wutsi.platform.core.stream.Event
import com.wutsi.platform.core.stream.EventHandler
import com.wutsi.platform.core.stream.EventStream
import com.wutsi.platform.core.stream.rabbitmq.RabbitMQEventStream
import com.wutsi.platform.core.stream.rabbitmq.RabbitMQHealthIndicator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.actuate.health.HealthIndicator
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Scheduled
import java.util.concurrent.ExecutorService

@Configuration
@ConditionalOnProperty(
    value = ["wutsi.platform.stream.type"],
    havingValue = "rabbitmq"
)
open class StreamConfigurationRabbitMQ(
    @Autowired
    private val eventPublisher: ApplicationEventPublisher,

    @Value("\${wutsi.platform.stream.name}") private val name: String,
    @Value("\${wutsi.platform.stream.subscriptions:}") subscriptions: Array<String>,
    @Value("\${wutsi.platform.stream.rabbitmq.url}") private val url: String,
    @Value("\${wutsi.platform.stream.rabbitmq.thread-pool-size:8}") private val threadPoolSize: Int,
    @Value("\${wutsi.platform.stream.rabbitmq.dlq.max-retries:10}") private val dlqMaxRetries: Int,
    @Value("\${wutsi.platform.stream.rabbitmq.queue-ttl-seconds:86400}") private val queueTtlSeconds: Long
) : AbstractStreamConfiguration(subscriptions) {
    @Bean
    open fun connectionFactory(): ConnectionFactory {
        val factory = ConnectionFactory()
        factory.setUri(url)
        return factory
    }

    @Bean(destroyMethod = "shutdown")
    open fun executorService(): ExecutorService =
        java.util.concurrent.Executors.newFixedThreadPool(threadPoolSize)

    @Bean(destroyMethod = "close")
    open fun channel(): Channel = connectionFactory()
        .newConnection(executorService())
        .createChannel()

    @Bean(destroyMethod = "close")
    override fun eventStream(): EventStream = RabbitMQEventStream(
        name = name,
        channel = channel(),
        queueTtlSeconds = queueTtlSeconds,
        dlqMaxRetries = dlqMaxRetries,
        handler = object : EventHandler {
            override fun onEvent(event: Event) {
                eventPublisher.publishEvent(event)
            }
        }
    )

    @Bean
    open fun rabbitMQHealthIndicator(): HealthIndicator =
        RabbitMQHealthIndicator(channel())

    @Scheduled(cron = "\${wutsi.platform.stream.rabbitmq.dlq.replay-cron:0 */15 * * * *}")
    public fun replayDlq() {
        (eventStream() as RabbitMQEventStream).replayDlq()
    }
}
