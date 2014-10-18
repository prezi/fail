package com.prezi.fail.api.queue

import org.junit.Test
import com.amazonaws.services.sqs.AmazonSQS
import com.prezi.fail.test.givenAny

class QueueTest {
    Test fun putCallsSQS() {
        val mockSQS = givenAny(javaClass<AmazonSQS>())
        val q = Queue(client = mockSQS)
    }
}