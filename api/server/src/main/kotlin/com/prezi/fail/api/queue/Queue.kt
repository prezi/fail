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

object Queue {
    val client = AmazonSQSClient()
    val url = client.getQueueUrl("fail-scheduled-failures")?.getQueueUrl()

    internal val logger = LoggerFactory.getLogger(javaClass)

    public fun putRun(r: Run) {
        val req = SendMessageRequest().withQueueUrl(url)
        client.sendMessage(req.withMessageBody(r.getId()))
    }

    public fun receiveRunAnd(action: (Run) -> Unit): Unit {
        val recvReq = ReceiveMessageRequest().withQueueUrl(url).withMaxNumberOfMessages(1)
        val recvMsg = client.receiveMessage(recvReq)
        val msg = recvMsg.getMessages().first
        if (msg == null) {
            logger.debug("Received empty message from SQS")
            return
        }
        val key = DBRun().setId(msg.getBody())
        val run = try {
            DB.mapper.load(key)?.model
        } catch (e: AmazonServiceException) {
            logger.error("unable to load Run from DB for id ${msg.getBody()} because ${e}")
            return
        }
        if (run == null) {
            logger.error("invalid_run_id_in_sqs ${msg.getBody()}")
            return
        }

        action(run)
    }
}