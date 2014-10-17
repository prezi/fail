package com.prezi.fail.api.db

import com.linkedin.data.template.StringMap
import com.linkedin.data.template.StringArray

import com.prezi.fail.api.db.DBRun
import com.prezi.fail.api.RunStatus


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
        println("Before save: id=${scheduledFailure.id} ${scheduledFailure.model}")

        DB.mapper.save(scheduledFailure)
        val loadedScheduledFailure = DB.mapper.load(javaClass<DBScheduledFailure>(), scheduledFailure.id)
        println("Read back from DB: id=${loadedScheduledFailure?.id} ${loadedScheduledFailure?.model}")

        val c = DBRun()
                .setAt(System.currentTimeMillis() / 1000)!!
                .setStatus(RunStatus.FUTURE)!!
                .setLog("Test log!")!!
                .setScheduledFailure(scheduledFailure)!!
        println("Before save: id=${c.id} ${c.model}")

        DB.mapper.save(c)
        val loadedCharge = DB.mapper.load(javaClass<DBRun>(), c.id)
        println("Read back from DB: id=${loadedCharge?.id} ${loadedCharge?.model}")

        println("ScheduledFailure of Charge read back from DB: ${loadedCharge?.getScheduledFailure(DB.mapper)?.model}")

    }
}
