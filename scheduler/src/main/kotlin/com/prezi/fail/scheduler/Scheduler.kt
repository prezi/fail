package com.prezi.fail.scheduler

import org.slf4j.LoggerFactory
import com.prezi.fail.api.Api
import com.prezi.fail.api.RunBuilders
import com.prezi.fail.api.queue.Queue
import com.prezi.fail.api.RunStatus


class Scheduler(val api: Api = Api(), val queue: Queue = Queue()) {
    val logger = LoggerFactory.getLogger(javaClass)!!
    val runInterval: Int = 5 * 60  // 5 minutes in s
    val queryAhead: Int = 24 * 3600 // 1 day in s

    public fun run() {
        while (true) {
            try {
                step()
            } catch (e: Throwable) {
                logger.error("error_during_queue_step ${e}")
            }
            Thread.sleep(runInterval.toLong() + 1)
        }
    }

    fun step() {
        logger.debug("Starting scheduling run")
        val queryAt = System.currentTimeMillis() / 1000
        val request = RunBuilders().findByTime()
                        .afterParam(queryAhead)
                        .atParam(queryAt)
        api.authenticate(request)
        val scheduleBefore = queryAt + runInterval
        val scheduledRuns = api.sendRequest(request.build())?.getElements()?.filter {
            it.getAt() <= scheduleBefore && it.getStatus() == RunStatus.FUTURE
        }
        logger.debug("Enqueueing ${scheduledRuns?.size ?: 0} elements")
        scheduledRuns?.forEach { run ->
            api.updateStatus(run, RunStatus.SCHEDULED)
            queue.putRun(run)
        }
    }
}
