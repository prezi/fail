package com.prezi.fail.api

import com.linkedin.restli.server.NettyStandaloneLauncher
import com.prezi.fail.api.db.DBUsageExample
import com.prezi.fail.config.loadUserProperties
import com.prezi.fail.api.db.DB
import com.prezi.fail.api.db.DBRun
import kotlin.reflect.KMemberFunction0
import java.lang.reflect.Method
import org.joda.time.DateTimeUtils
import org.slf4j.LoggerFactory

fun main(args: Array<String>) {
    loadUserProperties("/etc/prezi/fail-api/fail-api.properties")
    setCurrentTimeFixedIfProvidedInSystemProperties()
    if (args.size > 0 && args[0] == "dbtest") {
        DBUsageExample.run()
    } else {
        NettyStandaloneLauncher(8080, "com.prezi.fail.api.impl").start()
    }
}

fun setCurrentTimeFixedIfProvidedInSystemProperties() {
    val prop = "fail.test.fixedUnitTimestamp"
    val value = System.getProperty(prop)
    if (value != null) {
        LoggerFactory.getLogger("com.prezi.fail.api.main").warn("Pinning current time to ${value}. Use this only for testing! (like, obviously)")
        DateTimeUtils.setCurrentMillisFixed(value.toLong() * 1000)
    }
}
