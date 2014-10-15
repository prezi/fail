package com.prezi.fail.scheduler

import org.slf4j.LoggerFactory


class Scheduler {
    val logger = LoggerFactory.getLogger(javaClass)!!
    val runInterval: Long = 5 * 60 * 1000  // 5 minutes in ms

    public fun run() {
        while (true) {
            logger.debug("Starting scheduling run")
            Thread.sleep(runInterval)
        }
    }
}
