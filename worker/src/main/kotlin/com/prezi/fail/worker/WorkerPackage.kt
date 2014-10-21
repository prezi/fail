package com.prezi.fail.worker


import com.prezi.fail.config.loadUserProperties
import com.prezi.fail.config.updateLoggerLevels
import com.prezi.fail.config.FailConfig
import com.prezi.fail.api.Run
import com.prezi.fail.api.RunStatus
import com.prezi.fail.api.ScheduledFailure
import com.linkedin.data.template.StringMap
import com.linkedin.data.template.StringArray

fun main(args: Array<String>) {
    loadUserProperties("/etc/prezi/fail-worker/fail-worker.properties")
    updateLoggerLevels(FailConfig())
    if (args.size > 0 && args[0] == "fail-test") {
        doSingleFailureInjectionRun()
    } else {
        Worker().run()
    }
}

fun doSingleFailureInjectionRun() {
    val worker = Worker()
    worker.performFailureInjectionRun(
            Run()
                    .setId("8f37d3b9-5d94-41cb-ac2b-4ebe26f833b7")
                    .setAt(0)
                    .setStatus(RunStatus.FUTURE)
                    .setScheduledFailure(ScheduledFailure()
                        .setConfiguration(StringMap(mapOf()))
                        .setSearchTerm("service_name=hslogger-app")
                        .setDuration(2)
                        .setPeriod("every-hour")
                        .setSapper("noop")
                        .setSapperArgs(StringArray())
                    )
    )
}

