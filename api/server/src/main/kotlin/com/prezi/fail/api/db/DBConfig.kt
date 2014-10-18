package com.prezi.fail.api.db

import com.prezi.fail.config.Config

enum class DBConfigKey(val key: String) {
    DYNAMODB_ENDPOINT: DBConfigKey("fail.db.dynamoDBEndpoint")

    override fun toString() = key
}

open class DBConfig : Config<DBConfigKey>() {
    val DEFAULT_DYNAMODB_ENDPOINT = "dynamodb.us-east-1.amazonaws.com"

    fun getDynamoDBEndpoint(): String = getString(DBConfigKey.DYNAMODB_ENDPOINT) ?: DEFAULT_DYNAMODB_ENDPOINT

    override fun getToggledValue(key: DBConfigKey): String {
        throw UnsupportedOperationException()
    }
}
