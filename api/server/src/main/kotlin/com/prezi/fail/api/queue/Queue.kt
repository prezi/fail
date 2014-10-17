package com.prezi.fail.api.queue

import com.amazonaws.services.sqs.AmazonSQSClient
import com.prezi.fail.api.ScheduledFailure
import com.amazonaws.services.sqs.model.SendMessageRequest

object Queue {
    val client = AmazonSQSClient()
    val url = client.getQueueUrl("fail-scheduled-failures")?.getQueueUrl()

    public fun putScheduledFailure(f: ScheduledFailure) {

        val req = SendMessageRequest().withQueueUrl(url)
        client.sendMessage(req.withMessageBody(""))
    }
}