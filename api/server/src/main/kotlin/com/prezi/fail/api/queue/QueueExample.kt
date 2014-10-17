package com.prezi.fail.api.queue

fun main(args: Array<String>) {
    val qUrl = Queue.url
    val res = Queue.client.sendMessage(qUrl, "This is my item, woohoo!")
    Queue.client.sendMessage(qUrl, "This is my item2, woohoo!")
    Queue.client.sendMessage(qUrl, "This is my item3, woohoo!")
    println("SendMessageResult: ${res.toString()}")
    Queue.client.receiveMessage(com.amazonaws.services.sqs.model.ReceiveMessageRequest().withQueueUrl(qUrl).withMaxNumberOfMessages(1))
    val recv = Queue.client.receiveMessage(qUrl)?.getMessages()
    recv?.forEach { println("Item from queue: ${it.toString()}\n") }
}