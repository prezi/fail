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
                    .setAt(0)
                    .setStatus(RunStatus.FUTURE)
                    .setScheduledFailure(ScheduledFailure()
                        .setConfiguration(StringMap(mapOf("fail.cli.trace" to "true")))
                        .setSearchTerm("service_name=hslogger-app")
                        .setDuration(2)
                        .setPeriod("every-hour")
                        .setSapper("noop")
                        .setSapperArgs(StringArray())
                    )
    )
}

