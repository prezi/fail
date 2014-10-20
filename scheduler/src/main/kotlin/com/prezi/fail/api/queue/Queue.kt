package com.prezi.fail.api.queue

import com.amazonaws.services.sqs
import com.prezi.fail.api.Api
import com.prezi.fail.api.Run
import org.slf4j.LoggerFactory
import com.amazonaws.services.sqs.model
import com.prezi.fail.api.RunBuilders
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.AmazonSQSClient

class Queue(val client: AmazonSQS = AmazonSQSClient(), val name: String = "fail-scheduled-runs", val api: Api = Api()) {
    val url = client.getQueueUrl(name)?.getQueueUrl()

    internal val logger = LoggerFactory.getLogger(javaClass)

    public fun putRun(r: Run) {
        val req = model.SendMessageRequest().withQueueUrl(url)
        client.sendMessage(req.withMessageBody(r.getId()))
    }
}