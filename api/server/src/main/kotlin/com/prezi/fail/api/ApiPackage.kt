package com.prezi.fail.api

import com.linkedin.restli.server.NettyStandaloneLauncher
import com.prezi.fail.api.db.DBUsageExample
import com.prezi.fail.config.loadUserProperties

fun main(args: Array<String>) {
    loadUserProperties("/etc/prezi/fail-api/fail-api.properties")
    if (args.size > 0 && args[0] == "dbtest") {
        DBUsageExample.run()
    } else {
        NettyStandaloneLauncher(8080, "com.prezi.fail.api.impl").start()
    }
}
