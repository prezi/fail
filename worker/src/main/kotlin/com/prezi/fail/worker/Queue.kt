package com.prezi.fail.worker

import com.prezi.fail.api.Api
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.AmazonSQSClient
import org.slf4j.LoggerFactory
import com.prezi.fail.api.Run
import com.amazonaws.services.sqs.model.ReceiveMessageRequest
import com.prezi.fail.api.RunBuilders
import com.amazonaws.services.sqs.model.ChangeMessageVisibilityRequest


class Queue(val client: AmazonSQS = AmazonSQSClient(), val name: String = "fail-scheduled-runs", val api: Api = Api()) {
    val url = client.getQueueUrl(name)?.getQueueUrl()

    internal val logger = LoggerFactory.getLogger(javaClass)

    public fun receiveRunAnd(action: (Run) -> Unit): Unit {
        try {
            val recvReq = ReceiveMessageRequest().withQueueUrl(url).withMaxNumberOfMessages(1).withWaitTimeSeconds(10)
            val recvMsg = client.receiveMessage(recvReq)
            val msg = recvMsg.getMessages().first
            if (msg == null) {
                logger.debug("Received empty message from SQS")
                return
            }
            try {
                val request = RunBuilders().get().id(msg.getBody())
                api.authenticate(request)
                val run = api.sendRequest(request.build())
                if (run == null) {
                    logger.error("run_not_found ${msg.getBody()}")
                    client.deleteMessage(url, msg.getReceiptHandle())
                    return
                }
                val expectedDuration = run.getScheduledFailure().getDuration()
                client.changeMessageVisibility(ChangeMessageVisibilityRequest()
                        .withQueueUrl(url)
                        .withReceiptHandle(msg.getReceiptHandle())
                        .withVisibilityTimeout(expectedDuration))
                try {
                    action(run)
                } catch (e: Throwable) {
                    logger.error("exception_during_action ${e}")
                    return
                }
            } catch (e: Throwable) {
                logger.error("exception_during_message_handling ${e}")
            } finally {
                client.deleteMessage(url, msg.getReceiptHandle())
            }
        } catch (e: Throwable) {
            logger.error("exception_during_sqs_receive ${e}")
            return
        }
    }

}
