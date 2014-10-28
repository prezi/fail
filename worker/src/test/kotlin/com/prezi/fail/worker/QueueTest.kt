package com.prezi.fail.worker

import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.Matchers.any
import com.amazonaws.services.sqs.AmazonSQS
import com.prezi.fail.test.givenAny
import com.prezi.fail.api.Run
import com.prezi.fail.test.When
import com.prezi.fail.api.Api
import com.linkedin.restli.client.RestClient
import com.amazonaws.services.sqs.model.ChangeMessageVisibilityRequest
import kotlin.test.assertEquals
import com.prezi.fail.api.ScheduledFailure
import org.mockito.ArgumentCaptor
import com.linkedin.restli.client.Request
import com.amazonaws.services.sqs.model.ReceiveMessageRequest
import com.amazonaws.services.sqs.model.ReceiveMessageResult
import com.amazonaws.services.sqs.model.Message
import com.prezi.fail.constants.QUEUE_POISON_PILL

class MockApi(val ret: Any?) : Api() {
    override fun <T : Any> withClient(f: (RestClient) -> T?): T? = ret as T
}

class QueueTest {
    Test fun receiveSetsVisibilityTimeout() {
        val mockSQS = givenAny(javaClass<AmazonSQS>())
        val run = givenAny(javaClass<Run>())
        val mockApi = MockApi(run)
        val scheduledFailure = givenAny(javaClass<ScheduledFailure>())
        val duration = 10
        When(mockSQS.receiveMessage(any<ReceiveMessageRequest>())).thenReturn(ReceiveMessageResult().withMessages(Message().withBody("moo")))
        When(run.getScheduledFailure()).thenReturn(scheduledFailure)
        When(scheduledFailure.getDuration()).thenReturn(duration)
        val visibilityCaptor = ArgumentCaptor.forClass(javaClass<ChangeMessageVisibilityRequest>())
        val q = Queue(client = mockSQS, api = mockApi)
        q.receiveRunAnd({}, {})
        verify(mockSQS).changeMessageVisibility(visibilityCaptor.capture())
        assertEquals(duration, visibilityCaptor.getValue().getVisibilityTimeout())
    }

    Test fun actionNotCalledWithInvalidRun() {
        val mockSQS = givenAny(javaClass<AmazonSQS>())
        val mockApi = MockApi(null)
        val q = Queue(client = mockSQS, api = mockApi)
        q.receiveRunAnd({ assert(false, {"this should not be called"} )}, {})
    }

    Test fun receivePoisonPill() {
        val mockSQS = givenAny(javaClass<AmazonSQS>())
        val mockApi = MockApi(null)
        When(mockSQS.receiveMessage(any<ReceiveMessageRequest>())).thenReturn(ReceiveMessageResult().withMessages(Message().withBody(QUEUE_POISON_PILL)))
        val q = Queue(client = mockSQS, api = mockApi)
        val handler = givenAny(javaClass<kotlin.Function0<Unit>>())
        q.receiveRunAnd({}, handler)
        verify(handler, times(1))()
    }
}

