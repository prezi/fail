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
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.verify
import com.prezi.fail.api.queue

class QueueTest {
    Test fun putCallsSQS() {
        val mockSQS = givenAny(javaClass<AmazonSQS>())
        val run = givenAny(javaClass<Run>())
        val q = Queue(client = mockSQS)
        q.putRun(run)
        verify(mockSQS).sendMessage(any())
    }
}