package com.prezi.fail.api.db

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMarshalling
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey

enum class Flag(val default: Boolean) {
    FAKEFLAG: Flag(false)
    PANIC: Flag(false)

    fun get(mapper: DynamoDBMapper): Boolean = mapper.load(DBFlag(this))?.getValue() ?: default
    fun set(mapper: DynamoDBMapper, value: Boolean) = mapper.save(DBFlag(this, value))
}

[DynamoDBTable(tableName = "fail_Flag")]
class DBFlag(
        protected var _name: Flag = Flag.FAKEFLAG,
        protected var _value: Boolean = _name.default
) {
    [DynamoDBMarshalling(marshallerClass = javaClass<EnumMarshaller>())]
    [DynamoDBHashKey(attributeName = "Name")]
    public fun getName(): Flag? = _name
    public fun setName(v: Flag): DBFlag { _name = v; return this }

    [DynamoDBAttribute(attributeName = "Value")]
    public fun getValue(): Boolean = _value
    public fun setValue(v: Boolean): DBFlag { _value = v; return this }
}
