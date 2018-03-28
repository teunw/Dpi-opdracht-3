package nl.teun

import com.rabbitmq.client.Channel
import com.rabbitmq.client.ConnectionFactory
import kotlin.concurrent.thread

fun main(args: Array<String>) {

    val factory = ConnectionFactory()
    factory.host = HOST
    factory.port = PORT
    val connection = factory.newConnection()
    val channel = connection.createChannel()

    channel.exchangeDeclare(EXCHANGE_NAME, "fanout")
    val queue = channel.queueDeclare().queue

    val randomsentences = RandomSentenceService().getSentences()
    startEmittingThread(channel, randomsentences, "FIRST")
    startEmittingThread(channel, randomsentences, "SECOND")
    startEmittingThread(channel, randomsentences, "THIRD")
}

fun startEmittingThread(channel: Channel, randomsentences: List<String>, name: String) {
    thread {
        var i = 0
        while (!Thread.interrupted()) {
            channel.basicPublish("logs", "", null, "$name: ${i++}".toByteArray())
            Thread.sleep(1000)
        }
    }
}
