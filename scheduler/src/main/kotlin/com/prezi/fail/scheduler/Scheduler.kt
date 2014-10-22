package com.prezi.fail.scheduler

import org.slf4j.LoggerFactory
import com.prezi.fail.api.Api
import com.prezi.fail.api.RunBuilders
import com.prezi.fail.api.queue.Queue


class Scheduler(val api: Api = Api(), val queue: Queue = Queue()) {
    val logger = LoggerFactory.getLogger(javaClass)!!
    val runInterval: Long = 5 * 60 * 1000  // 5 minutes in ms

    public fun run() {
        while (true) {
            try {
                step()
            } catch (e: Throwable) {
                logger.error("error_during_queue_step ${e}")
            }
            Thread.sleep(runInterval)
        }
    }

    fun step() {
        logger.debug("Starting scheduling run")
        val request = RunBuilders().findByTime()
                        .afterParam((runInterval / 1000).toInt())
                        .atParam(System.currentTimeMillis() / 1000)
        api.authenticate(request)
        val scheduledRuns = api.sendRequest(request.build())?.getElements()
        logger.debug("Enqueueing ${scheduledRuns?.size ?: 0} elements")
        scheduledRuns?.forEach { run -> queue.putRun(run) }
    }
}
