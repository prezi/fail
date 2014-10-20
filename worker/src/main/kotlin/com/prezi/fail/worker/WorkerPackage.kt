package com.prezi.fail.worker


import com.prezi.fail.config.loadUserProperties

fun main(args: Array<String>) {
    loadUserProperties("/etc/prezi/fail-worker/fail-worker.properties")
    Worker().run()
}

