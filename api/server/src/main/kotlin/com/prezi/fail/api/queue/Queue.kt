package com.prezi.fail.api.queue

import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.AmazonSQSClient
import com.amazonaws.services.sqs.model
import org.slf4j.LoggerFactory
import com.prezi.fail.constants.QUEUE_POISON_PILL
import com.amazonaws.regions.Region
import com.amazonaws.services.sqs.model.SetQueueAttributesRequest
import com.amazonaws.services.sqs.model.ChangeMessageVisibilityBatchRequest
import com.amazonaws.services.sqs.model.ChangeMessageVisibilityBatchResult
import com.amazonaws.services.sqs.model.ChangeMessageVisibilityRequest
import com.amazonaws.services.sqs.model.GetQueueUrlRequest
import com.amazonaws.services.sqs.model.GetQueueUrlResult
import com.amazonaws.services.sqs.model.RemovePermissionRequest
import com.amazonaws.services.sqs.model.GetQueueAttributesRequest
import com.amazonaws.services.sqs.model.GetQueueAttributesResult
import com.amazonaws.services.sqs.model.SendMessageBatchRequest
import com.amazonaws.services.sqs.model.SendMessageBatchResult
import com.amazonaws.services.sqs.model.ListDeadLetterSourceQueuesRequest
import com.amazonaws.services.sqs.model.ListDeadLetterSourceQueuesResult
import com.amazonaws.services.sqs.model.DeleteQueueRequest
import com.amazonaws.services.sqs.model.SendMessageRequest
import com.amazonaws.services.sqs.model.SendMessageResult
import com.amazonaws.services.sqs.model.ReceiveMessageRequest
import com.amazonaws.services.sqs.model.ReceiveMessageResult
import com.amazonaws.services.sqs.model.ListQueuesRequest
import com.amazonaws.services.sqs.model.ListQueuesResult
import com.amazonaws.services.sqs.model.DeleteMessageBatchRequest
import com.amazonaws.services.sqs.model.DeleteMessageBatchResult
import com.amazonaws.services.sqs.model.CreateQueueRequest
import com.amazonaws.services.sqs.model.CreateQueueResult
import com.amazonaws.services.sqs.model.AddPermissionRequest
import com.amazonaws.services.sqs.model.DeleteMessageRequest
import com.amazonaws.services.sqs.model.ChangeMessageVisibilityBatchRequestEntry
import com.amazonaws.services.sqs.model.SendMessageBatchRequestEntry
import com.amazonaws.services.sqs.model.DeleteMessageBatchRequestEntry
import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.ResponseMetadata

fun sqsClientFactory(): AmazonSQS {
    if (System.getProperty("fail.test.useMockSQS", "0") == "1") {
        return AmazonSQSMock()
    } else {
        return AmazonSQSClient()
    }
}

class Queue(val client: AmazonSQS = sqsClientFactory(), val name: String = "fail-scheduled-runs") {
    val url = client.getQueueUrl(name)?.getQueueUrl()

    internal val logger = LoggerFactory.getLogger(javaClass)

    public fun putPoisonPill() {
        val req = model.SendMessageRequest().withQueueUrl(url)
        client.sendMessage(req.withMessageBody(QUEUE_POISON_PILL))
    }
}
