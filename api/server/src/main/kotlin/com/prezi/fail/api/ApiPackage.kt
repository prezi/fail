package com.prezi.fail.api

import com.linkedin.restli.server.NettyStandaloneLauncher
import com.prezi.fail.api.db.DBUsageExample
import com.prezi.fail.loadUserProperties

fun main(args: Array<String>) {
    if (args.size > 0 && args[0] == "dbtest") {
        DBUsageExample.run()
    } else {
        loadUserProperties("/etc/prezi/fail-api/fail-api.properties")
        NettyStandaloneLauncher(8080, "com.prezi.fail.api.impl").start()
    }
}
