package com.prezi.fail.api.queue

import com.amazonaws.services.sqs.AmazonSQSClient
import com.prezi.fail.api.ScheduledFailure
import com.amazonaws.services.sqs.model.SendMessageRequest
import com.prezi.fail.api.Run
import com.amazonaws.services.sqs.model.ReceiveMessageRequest
import com.prezi.fail.api.db.DB
import com.prezi.fail.api.db.DBRun
import org.slf4j.LoggerFactory
import com.amazonaws.AmazonServiceException
import com.prezi.fail.api.Api
import com.prezi.fail.api.RunBuilders
import com.amazonaws.services.sqs.model.ChangeMessageVisibilityRequest
import com.amazonaws.services.sqs.AmazonSQS

class Queue(val client: AmazonSQS = AmazonSQSClient(), val name: String = "fail-scheduled-runs", val api: Api = Api()) {
    val url = client.getQueueUrl(name)?.getQueueUrl()

    internal val logger = LoggerFactory.getLogger(javaClass)

    public fun putRun(r: Run) {
        val req = SendMessageRequest().withQueueUrl(url)
        client.sendMessage(req.withMessageBody(r.getId()))
    }
}