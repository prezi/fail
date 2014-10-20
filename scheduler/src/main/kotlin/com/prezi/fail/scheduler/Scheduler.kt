package com.prezi.fail.scheduler

import org.slf4j.LoggerFactory
import com.prezi.fail.api.Api
import com.prezi.fail.api.RunBuilders


class Scheduler(val api: Api = Api()) {
    val logger = LoggerFactory.getLogger(javaClass)!!
    val runInterval: Long = 5 * 60 * 1000  // 5 minutes in ms

    public fun run() {
        while (true) {
            step()
            Thread.sleep(runInterval)
        }
    }

    fun step() {
        logger.debug("Starting scheduling run")
        api.sendRequest(RunBuilders().findByTime()
                .afterParam((runInterval / 1000).toInt())
                .build())
    }
}
