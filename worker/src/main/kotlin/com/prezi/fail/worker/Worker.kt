package com.prezi.fail.worker

import org.slf4j.LoggerFactory
import com.prezi.fail.api.Run

public class Worker {
    val logger = LoggerFactory.getLogger(javaClass)!!
    val queue = Queue()

    public fun run() {
        while(true) {
            queue.receiveRunAnd { performFailureInjectionRun(it) }
        }
    }

    fun performFailureInjectionRun(run: Run) {
        logger.info("This is where I'd perform ${run}")
    }
}