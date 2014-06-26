package com.prezi.anthro.sarge

import org.slf4j.LoggerFactory
import com.jcraft.jsch.SftpProgressMonitor

class LoggingSftpProgressMonitor : SftpProgressMonitor {
    val logger = LoggerFactory.getLogger(this.javaClass)!!
    var op: Int = 0
    var src: String? = null
    var dest: String? = null
    var max: Long = 0
    var done: Long = 0

    fun opToString(op: Int): String = when (op) {
        SftpProgressMonitor.GET -> "GET"
        SftpProgressMonitor.PUT -> "PUT"
        else                    -> "UNKNOWN-ACTION"
    }

    fun log(msg: String) {
        logger.info("${opToString(op)} ${src} ${dest} ${msg}")
    }

    override fun init(op: Int, src: String?, dest: String?, max: Long) {
        this.op = op
        this.max = max
        this.src = src
        this.dest = dest
        this.max = max
        log("init")
    }

    override fun count(count: Long): Boolean {
        done += count
        log("${ (done.toFloat() / max.toFloat() * 100).toInt() }% (${done}/${max})")
        return true
    }

    override fun end() {
        log("end")
    }

}