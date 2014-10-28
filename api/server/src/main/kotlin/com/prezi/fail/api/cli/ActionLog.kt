package com.prezi.fail.api.cli

import org.slf4j.LoggerFactory
import com.prezi.fail.cli.Action
import com.prezi.fail.api.db.Flag
import com.prezi.fail.api.db.DB
import com.prezi.fail.api.impl.RunResource
import org.joda.time.DateTime
import com.prezi.fail.api.RunStatus
import com.prezi.fail.api.queue.Queue
import com.prezi.fail.api.db.DBRun


public class ActionLog(val runId: String, val db: DB = DB()): Action() {
    val logger = LoggerFactory.getLogger(javaClass)!!


    class object {
        val requiredArgCount = 1
        val verb = "log"
        val cmdLineSyntax = "${verb} <run-id>"
    }

    override fun run() {
        val run = db.mapper.load(javaClass<DBRun>(), runId)
        if (run == null) {
            logger.warn("No run found with id ${runId}. You can use `fail list-runs --before PT3H` to get a list of recent runs.")
            return
        }
        logger.info("Status: ${run.getStatus()}")
        logger.info("Log:\n${run.getLog()}")
    }
}