package nl.teun

import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import java.nio.charset.Charset

const val EXCHANGE_NAME = "logs"
const val HOST = "teunwillems.nl"
const val PORT = 5672

fun main(args: Array<String>) {
    val factory = ConnectionFactory()
    factory.host = HOST
    factory.port = PORT
    val connection = factory.newConnection()
    val channel = connection.createChannel()

    channel.exchangeDeclare(EXCHANGE_NAME, "fanout")
    val queueName = channel.queueDeclare().queue
    channel.queueBind(queueName, EXCHANGE_NAME, "")

    println(" [*] Waiting for messages. To exit press CTRL+C")

    channel.basicConsume(
            queueName,
            true,
            DeliverCallback { _, message -> println(String(message.body, Charset.defaultCharset())) }, null, null)
}