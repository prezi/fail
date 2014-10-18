package com.prezi.fail.api.queue

import com.prezi.fail.api.db.DBRun
import com.prezi.fail.api.RunStatus
import com.prezi.fail.api.db.DBScheduledFailure
import com.linkedin.data.template.StringArray
import com.linkedin.data.template.StringMap
import com.prezi.fail.api.db.DB
import com.prezi.fail.api.Api
import com.prezi.fail.api.ScheduledFailureBuilders

fun main(args: Array<String>) {
    val scheduledFailure = DBScheduledFailure()
            .setPeriod("test-interval-42")!!
            .setSapper("noop")!!
            .setSearchTerm("hslogger-app")!!
            .setConfiguration(StringMap())!!
            .setDuration(30)!!
            .setSapperArgs(StringArray())!!
            .setScheduledAt(42)!!
            .setScheduledBy("not-abesto")!!
    DB.mapper.save(scheduledFailure)

    val item = DBRun().setAt(System.currentTimeMillis() / 1000)?.setStatus(RunStatus.FAILED)?.setLog("testlog")?.setScheduledFailure(scheduledFailure)!!
    DB.mapper.save(item)
    println("item id: ${item.getId()}")

    val q = Queue()
    q.putRun(item.model)
    q.receiveRunAnd { println("Got run from queue ${it}") }

}