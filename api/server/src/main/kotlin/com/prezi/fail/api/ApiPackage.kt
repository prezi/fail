package com.prezi.fail.api

import com.linkedin.restli.server.NettyStandaloneLauncher

fun main(args: Array<String>) {
    NettyStandaloneLauncher(8080, "com.prezi.fail.api.impl").start()
}
