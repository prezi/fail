package com.prezi.fail.api.db

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper

object DB {
    val client = AmazonDynamoDBClient()
    val mapper = DynamoDBMapper(client)
}
