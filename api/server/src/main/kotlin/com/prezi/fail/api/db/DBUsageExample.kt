package com.prezi.fail.api.db

import com.linkedin.data.template.StringMap
import com.linkedin.data.template.StringArray

import com.prezi.fail.api.RunStatus
import org.joda.time.DateTime


object DBUsageExample {
    fun run() {
        val conf = StringMap()
        conf.set("foo", "bar")

        val scheduledFailure = DBScheduledFailure()
                .setPeriod("test-interval-1")!!
                .setSapper("noop")!!
                .setSearchTerm("hslogger-app")!!
                .setConfiguration(conf)!!
                .setDuration(30)!!
                .setSapperArgs(StringArray())!!
                .setScheduledAt(42)!!
                .setScheduledBy("abesto")!!
        println("Before save: id=${scheduledFailure.getId()} ${scheduledFailure.model}")

        val db = DB()
        db.mapper.save(scheduledFailure)
        val loadedScheduledFailure = db.mapper.load(javaClass<DBScheduledFailure>(), scheduledFailure.getId())
        println("Read back from DB: id=${loadedScheduledFailure?.getId()} ${loadedScheduledFailure?.model}")

        val c = DBRun()
                .setAtMillis(DateTime.now().getMillis())!!
                .setStatus(RunStatus.FUTURE)!!
                .setLog("Test log!")!!
                .setScheduledFailure(scheduledFailure)!!
        println("Before save: ${c.model}")

        db.mapper.save(c)
        val loadedRun = db.mapper.load(javaClass<DBRun>(), c.getId())
        println("Read back from DB: ${loadedRun?.model}")

        println("ScheduledFailure of run read back from DB: ${loadedRun?.getScheduledFailure(db.mapper)?.model}")

    }
}
