package com.prezi.fail.api.queue

import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.ec2.model.Region
import com.amazonaws.regions
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

class AmazonSQSMock: AmazonSQS {
    override fun setEndpoint(endpoint: String?) {
        throw UnsupportedOperationException()
    }

    override fun setRegion(region: regions.Region?) {
        throw UnsupportedOperationException()
    }

    override fun setQueueAttributes(setQueueAttributesRequest: SetQueueAttributesRequest?) {
        throw UnsupportedOperationException()
    }

    override fun changeMessageVisibilityBatch(changeMessageVisibilityBatchRequest: ChangeMessageVisibilityBatchRequest?): ChangeMessageVisibilityBatchResult? {
        throw UnsupportedOperationException()
    }

    override fun changeMessageVisibility(changeMessageVisibilityRequest: ChangeMessageVisibilityRequest?) {
        throw UnsupportedOperationException()
    }

    override fun getQueueUrl(getQueueUrlRequest: GetQueueUrlRequest?): GetQueueUrlResult? {
        throw UnsupportedOperationException()
    }

    override fun removePermission(removePermissionRequest: RemovePermissionRequest?) {
        throw UnsupportedOperationException()
    }

    override fun getQueueAttributes(getQueueAttributesRequest: GetQueueAttributesRequest?): GetQueueAttributesResult? {
        throw UnsupportedOperationException()
    }

    override fun sendMessageBatch(sendMessageBatchRequest: SendMessageBatchRequest?): SendMessageBatchResult? {
        throw UnsupportedOperationException()
    }

    override fun listDeadLetterSourceQueues(listDeadLetterSourceQueuesRequest: ListDeadLetterSourceQueuesRequest?): ListDeadLetterSourceQueuesResult? {
        throw UnsupportedOperationException()
    }

    override fun deleteQueue(deleteQueueRequest: DeleteQueueRequest?) {
        throw UnsupportedOperationException()
    }

    override fun sendMessage(sendMessageRequest: SendMessageRequest?): SendMessageResult? {
        return null
    }

    override fun receiveMessage(receiveMessageRequest: ReceiveMessageRequest?): ReceiveMessageResult? {
        throw UnsupportedOperationException()
    }

    override fun listQueues(listQueuesRequest: ListQueuesRequest?): ListQueuesResult? {
        throw UnsupportedOperationException()
    }

    override fun deleteMessageBatch(deleteMessageBatchRequest: DeleteMessageBatchRequest?): DeleteMessageBatchResult? {
        throw UnsupportedOperationException()
    }

    override fun createQueue(createQueueRequest: CreateQueueRequest?): CreateQueueResult? {
        throw UnsupportedOperationException()
    }

    override fun addPermission(addPermissionRequest: AddPermissionRequest?) {
        throw UnsupportedOperationException()
    }

    override fun deleteMessage(deleteMessageRequest: DeleteMessageRequest?) {
        throw UnsupportedOperationException()
    }

    override fun listQueues(): ListQueuesResult? {
        throw UnsupportedOperationException()
    }

    override fun setQueueAttributes(queueUrl: String?, attributes: MutableMap<String, String>?) {
        throw UnsupportedOperationException()
    }

    override fun changeMessageVisibilityBatch(queueUrl: String?, entries: MutableList<ChangeMessageVisibilityBatchRequestEntry>?): ChangeMessageVisibilityBatchResult? {
        throw UnsupportedOperationException()
    }

    override fun changeMessageVisibility(queueUrl: String?, receiptHandle: String?, visibilityTimeout: Int?) {
        throw UnsupportedOperationException()
    }

    override fun getQueueUrl(queueName: String?): GetQueueUrlResult? {
        return null
    }

    override fun removePermission(queueUrl: String?, label: String?) {
        throw UnsupportedOperationException()
    }

    override fun getQueueAttributes(queueUrl: String?, attributeNames: MutableList<String>?): GetQueueAttributesResult? {
        throw UnsupportedOperationException()
    }

    override fun sendMessageBatch(queueUrl: String?, entries: MutableList<SendMessageBatchRequestEntry>?): SendMessageBatchResult? {
        throw UnsupportedOperationException()
    }

    override fun deleteQueue(queueUrl: String?) {
        throw UnsupportedOperationException()
    }

    override fun sendMessage(queueUrl: String?, messageBody: String?): SendMessageResult? {
        throw UnsupportedOperationException()
    }

    override fun receiveMessage(queueUrl: String?): ReceiveMessageResult? {
        throw UnsupportedOperationException()
    }

    override fun listQueues(queueNamePrefix: String?): ListQueuesResult? {
        throw UnsupportedOperationException()
    }

    override fun deleteMessageBatch(queueUrl: String?, entries: MutableList<DeleteMessageBatchRequestEntry>?): DeleteMessageBatchResult? {
        throw UnsupportedOperationException()
    }

    override fun createQueue(queueName: String?): CreateQueueResult? {
        throw UnsupportedOperationException()
    }

    override fun addPermission(queueUrl: String?, label: String?, aWSAccountIds: MutableList<String>?, actions: MutableList<String>?) {
        throw UnsupportedOperationException()
    }

    override fun deleteMessage(queueUrl: String?, receiptHandle: String?) {
        throw UnsupportedOperationException()
    }

    override fun shutdown() {
        throw UnsupportedOperationException()
    }

    override fun getCachedResponseMetadata(request: AmazonWebServiceRequest?): ResponseMetadata? {
        throw UnsupportedOperationException()
    }
}
