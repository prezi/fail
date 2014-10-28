package com.prezi.fail.api.queue

import org.junit.Test
import org.mockito.Mockito.*
import com.amazonaws.services.sqs.AmazonSQS
import com.prezi.fail.test.givenAny
import kotlin.test.assertEquals
import org.mockito.ArgumentCaptor
import com.prezi.fail.constants.QUEUE_POISON_PILL
import com.amazonaws.services.sqs.model.SendMessageRequest

class QueueTest {
    Test fun putCallsSQS() {
        val mockSQS = givenAny(javaClass<AmazonSQS>())
        val q = Queue(client = mockSQS)
        val requestCaptor = ArgumentCaptor.forClass(javaClass<SendMessageRequest>())
        q.putPoisonPill()
        verify(mockSQS, times(1)).sendMessage(requestCaptor.capture())
        assertEquals(QUEUE_POISON_PILL, requestCaptor.getValue().getMessageBody())
    }
}

