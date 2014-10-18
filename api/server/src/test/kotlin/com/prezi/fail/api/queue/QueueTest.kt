package com.prezi.fail.api.queue

import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.Matchers.*
import com.amazonaws.services.sqs.AmazonSQS
import com.prezi.fail.test.givenAny
import com.prezi.fail.api.Run
import com.prezi.fail.test.When
import com.prezi.fail.api.Api
import com.linkedin.restli.client.RestClient
import org.mockito.ArgumentCaptor
import com.amazonaws.services.sqs.model.ChangeMessageVisibilityRequest
import kotlin.test.assertEquals
import com.prezi.fail.api.ScheduledFailure

fun protoClientAction(c: RestClient): Run = Run()

class MockApi(val ret: Any?) : Api() {
    override fun <T : Any> withClient(f: (RestClient) -> T): T? = ret as T
}

class QueueTest {
    Test fun putCallsSQS() {
        val mockSQS = givenAny(javaClass<AmazonSQS>())
        val run = givenAny(javaClass<Run>())
        val q = Queue(client = mockSQS)
        q.putRun(run)
        verify(mockSQS).sendMessage(any())
    }

    Test fun receiveSetsVisibilityTimeout() {
        val mockSQS = givenAny(javaClass<AmazonSQS>())
        val run = givenAny(javaClass<Run>())
        val mockApi = MockApi(run)
        val scheduledFailure = givenAny(javaClass<ScheduledFailure>())
        val duration = 10
        When(run.getScheduledFailure()).thenReturn(scheduledFailure)
        When(scheduledFailure.getDuration()).thenReturn(duration)
        val visibilityCaptor = ArgumentCaptor.forClass(javaClass<ChangeMessageVisibilityRequest>())
        val q = Queue(client = mockSQS, api = mockApi)
        q.receiveRunAnd {  }
        verify(mockSQS).changeMessageVisibility(visibilityCaptor.capture())
        assertEquals(duration, visibilityCaptor.getValue().getVisibilityTimeout())
    }

    Test fun actionNotCalledWithInvalidRun() {
        val mockSQS = givenAny(javaClass<AmazonSQS>())
        val mockApi = MockApi(null)
        val q = Queue(client = mockSQS, api = mockApi)
        q.receiveRunAnd { assert(false, {"this should not be called"} )}
    }
}