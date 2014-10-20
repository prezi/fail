package com.prezi.fail.worker


import com.prezi.fail.config.loadUserProperties
import com.prezi.fail.config.updateLoggerLevels
import com.prezi.fail.config.FailConfig

fun main(args: Array<String>) {
    loadUserProperties("/etc/prezi/fail-worker/fail-worker.properties")
    updateLoggerLevels(FailConfig())
    Worker().run()
}

