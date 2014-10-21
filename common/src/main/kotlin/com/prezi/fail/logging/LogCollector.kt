package com.prezi.fail.logging

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import org.slf4j.LoggerFactory
import ch.qos.logback.core.Context
import org.slf4j.Logger
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import org.apache.commons.io.output.ByteArrayOutputStream

class LogCollector {
    val logAppender = ListAppender<ILoggingEvent>()
    val encoderPattern = "%d{HH:mm:ss.SSS} %.-1level %msg%n"

    var started = false
    var stopped = false

    fun start(): LogCollector {
        if (started) { throw IllegalStateException("LogCollector.start calleb, but it's already started") }
        started = true
        logAppender.start()
        logAppender.setContext(getContext())
        (LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as ch.qos.logback.classic.Logger).addAppender(logAppender)
        return this
    }

    private fun getContext(): Context {
        return LoggerFactory.getILoggerFactory() as Context
    }

    fun stopAndGetMessages(): List<ILoggingEvent> {
        if (!started) { throw IllegalStateException("LogCollector.stopAndGetMessages called, but it's not yet started") }
        if (stopped) { throw IllegalStateException("LogCollector.stopAndGetMessages called, but it's already stopped") }
        stopped = true
        (LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as ch.qos.logback.classic.Logger).detachAppender(logAppender)
        return logAppender.list!!
    }

    fun stopAndGetEncodedMessages(): String {
        val stream = ByteArrayOutputStream()
        val encoder = PatternLayoutEncoder()
        encoder.setPattern(encoderPattern)
        encoder.init(stream)
        encoder.setContext(getContext())
        encoder.start()
        stopAndGetMessages().forEach { encoder.doEncode(it) }
        encoder.stop()
        return stream.toString()
    }
}
