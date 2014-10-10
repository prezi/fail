package com.prezi.fail.api.cli

import org.apache.commons.cli.Option
import com.prezi.fail.Config

enum class ApiCliConfigKey(val key: String, val opt: Option) {
    API_ENDPOINT : ApiCliConfigKey("fail.cli.apiEndpoint", Option(null, "api", true, "URL prefix to the Fail API"))
    DEBUG        : ApiCliConfigKey("fail.cli.debug", Option("v", "debug", false, "Set root logger to DEBUG level"))
    TRACE        : ApiCliConfigKey("fail.cli.trace", Option("vv", "trace", false, "Set root logger to TRACE level"))
    DRY_RUN      : ApiCliConfigKey("fail.dryRun", Option("n", "dry-run", false, "Don't actually do any non-read-only actions"))

    LIST_BEFORE  : ApiCliConfigKey("fail.cli.listJobs.before", Option(null, "before", true, "list: Show scheduled jobs this far in the future"))
    LIST_AFTER   : ApiCliConfigKey("fail.cli.listJobs.after", Option(null,  "after", true, "list: Show scheduled jobs this far in the past"))
    LIST_CONTEXT : ApiCliConfigKey("fail.cli.listJobs.context", Option(null, "context", true, "list: Show scheduled jobs this far both in the future and the past"))
    LIST_AT      : ApiCliConfigKey("fail.cli.listJobs.at", Option(null, "at", true, "list: Show scheduled round this unix timestamp"))
    override fun toString() = key
}

open class ApiCliConfig : Config<ApiCliConfigKey>() {
    val DEFAULT_API_ENDPOINT = "http://localhost:8080/"
    val DEFAULT_DEBUG = false
    val DEFAULT_TRACE = false
    val DEFAULT_DRY_RUN = false

    val DEFAULT_LIST_BEFORE = null
    val DEFAULT_LIST_AFTER = null
    val DEFAULT_LIST_CONTEXT = null
    val DEFAULT_LIST_AT = { System.currentTimeMillis() / 1000 }

    open fun getApiEndpoint(): String = getString(ApiCliConfigKey.API_ENDPOINT) ?: DEFAULT_API_ENDPOINT
    open fun isDebug(): Boolean = getBool(ApiCliConfigKey.DEBUG, DEFAULT_DEBUG)
    open fun isTrace(): Boolean = getBool(ApiCliConfigKey.TRACE, DEFAULT_TRACE)
    open fun isDryRun() = getBool(ApiCliConfigKey.DRY_RUN, DEFAULT_DRY_RUN)

    open fun getListBefore(): String? = getString(ApiCliConfigKey.LIST_BEFORE) ?: DEFAULT_LIST_BEFORE
    open fun getListAfter(): String? = getString(ApiCliConfigKey.LIST_AFTER) ?: DEFAULT_LIST_AFTER
    open fun getListContext(): String? = getString(ApiCliConfigKey.LIST_CONTEXT) ?: DEFAULT_LIST_CONTEXT
    open fun getListAt(): Long = getString(ApiCliConfigKey.LIST_AT)?.toLong() ?: DEFAULT_LIST_AT()

    override public fun getToggledValue(key: ApiCliConfigKey): String {
        return when (key) {
            ApiCliConfigKey.DRY_RUN -> (!DEFAULT_DRY_RUN).toString()
            ApiCliConfigKey.DEBUG -> (!DEFAULT_DEBUG).toString()
            ApiCliConfigKey.TRACE -> (!DEFAULT_TRACE).toString()
            else -> throw RuntimeException("SargeConfig.getToggledValue called with invalid key ${key}")
        }
    }
}
