package com.prezi.fail.api.db

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression

object DB {
    val client = AmazonDynamoDBClient()
    val mapper = DynamoDBMapper(client)

    public fun loadAllScheduledFailures(): List<DBScheduledFailure> =
            DB.mapper.scan(javaClass<DBScheduledFailure>(), DynamoDBScanExpression()).toList()

}
