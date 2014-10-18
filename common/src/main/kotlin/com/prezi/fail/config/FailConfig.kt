package com.prezi.fail.config

import org.apache.commons.cli.Option
import org.joda.time.DateTime
import org.joda.time.DateTimeConstants

enum class FailConfigKey(val key: String, val opt: Option) {
    API_ENDPOINT : FailConfigKey("fail.cli.apiEndpoint", Option(null, "api", true, "URL prefix to the Fail API"))
    DEBUG        : FailConfigKey("fail.cli.debug", Option("v", "debug", false, "Set root logger to DEBUG level"))
    TRACE        : FailConfigKey("fail.cli.trace", Option("vv", "trace", false, "Set root logger to TRACE level"))
    DRY_RUN      : FailConfigKey("fail.dryRun", Option("n", "dry-run", false, "Don't actually do any non-read-only actions"))

    LIST_BEFORE  : FailConfigKey("fail.cli.listJobs.before", Option(null, "before", true, "list: Show scheduled jobs this far in the future"))
    LIST_AFTER   : FailConfigKey("fail.cli.listJobs.after", Option(null,  "after", true, "list: Show scheduled jobs this far in the past"))
    LIST_CONTEXT : FailConfigKey("fail.cli.listJobs.context", Option(null, "context", true, "list: Show scheduled jobs this far both in the future and the past"))
    LIST_AT      : FailConfigKey("fail.cli.listJobs.at", Option(null, "at", true, "list: Show scheduled round this unix timestamp"))

    DATETIME_FORMAT : FailConfigKey("fail.cli.dateTimeFormat", Option(null, "datetime-format", true, ""))
    override fun toString() = key
}

open class FailConfig : Config<FailConfigKey>() {
    val DEFAULT_API_ENDPOINT = "http://localhost:8080/"
    val DEFAULT_DEBUG = false
    val DEFAULT_TRACE = false
    val DEFAULT_DRY_RUN = false

    val DEFAULT_LIST_BEFORE = null
    val DEFAULT_LIST_AFTER = null
    val DEFAULT_LIST_CONTEXT = null
    val DEFAULT_LIST_AT = { DateTime.now().getMillis() / DateTimeConstants.MILLIS_PER_SECOND }

    val DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss z"

    open fun getApiEndpoint(): String = getString(FailConfigKey.API_ENDPOINT) ?: DEFAULT_API_ENDPOINT
    open fun isDebug(): Boolean = getBool(FailConfigKey.DEBUG, DEFAULT_DEBUG)
    open fun isTrace(): Boolean = getBool(FailConfigKey.TRACE, DEFAULT_TRACE)
    open fun isDryRun() = getBool(FailConfigKey.DRY_RUN, DEFAULT_DRY_RUN)

    open fun getListBefore(): String? = getString(FailConfigKey.LIST_BEFORE) ?: DEFAULT_LIST_BEFORE
    open fun getListAfter(): String? = getString(FailConfigKey.LIST_AFTER) ?: DEFAULT_LIST_AFTER
    open fun getListContext(): String? = getString(FailConfigKey.LIST_CONTEXT) ?: DEFAULT_LIST_CONTEXT
    open fun getListAt(): Long = getString(FailConfigKey.LIST_AT)?.toLong() ?: DEFAULT_LIST_AT()

    open fun getDatetimeFormat(): String = getString(FailConfigKey.DATETIME_FORMAT) ?: DEFAULT_DATETIME_FORMAT

    override public fun getToggledValue(key: FailConfigKey): String {
        return when (key) {
            FailConfigKey.DRY_RUN -> (!DEFAULT_DRY_RUN).toString()
            FailConfigKey.DEBUG -> (!DEFAULT_DEBUG).toString()
            FailConfigKey.TRACE -> (!DEFAULT_TRACE).toString()
            else -> throw RuntimeException("SargeConfig.getToggledValue called with invalid key ${key}")
        }
    }
}
