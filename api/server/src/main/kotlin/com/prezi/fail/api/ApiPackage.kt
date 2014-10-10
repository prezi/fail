package com.prezi.fail.api

import com.linkedin.restli.server.NettyStandaloneLauncher
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import java.util.Arrays
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.prezi.fail.api.db.DBScheduledFailure
import com.linkedin.data.template.StringMap
import com.linkedin.data.template.StringArray

fun main(args: Array<String>) {
    val client = AmazonDynamoDBClient(ProfileCredentialsProvider())
    val mapper = DynamoDBMapper(client)

    val conf = StringMap()
    conf.set("foo", "bar")

    val item = DBScheduledFailure()
            .setInterval("test-interval-1")!!
            .setSapper("noop")!!
            .setSearchTerm("hslogger-app")!!
            .setConfiguration(conf)!!
            .setDuration(30)!!
            .setSapperArgs(StringArray())!!
            .setScheduledAt(42)!!
            .setScheduledBy("abesto")!!
    println("Before save: id=${item.id} ${item.model}")

    mapper.save(item)
    val loadedItem = mapper.load(javaClass< DBScheduledFailure>(), item.id)
    println("Read back from DB: id=${loadedItem?.id} ${loadedItem?.model}")

    //NettyStandaloneLauncher(8080, "com.prezi.fail.api.impl").start()
}
