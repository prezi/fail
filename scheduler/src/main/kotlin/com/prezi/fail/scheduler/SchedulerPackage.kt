package com.prezi.fail.scheduler

import com.prezi.fail.loadUserProperties

fun main(args: Array<String>) {
    loadUserProperties("/etc/prezi/fail-scheduler/fail-scheduler.properties")
    Scheduler().run()
}
