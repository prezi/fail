package com.prezi.fail.api.db

import com.linkedin.restli.server.NettyStandaloneLauncher
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import java.util.Arrays
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.prezi.fail.api.db.DBScheduledFailure
import com.linkedin.data.template.StringMap
import com.linkedin.data.template.StringArray
import com.prezi.fail.api.db.DBCharge
import com.prezi.fail.api.ChargeStatus


object DBUsageExample {
    fun run() {
        val client = AmazonDynamoDBClient()
        val mapper = DynamoDBMapper(client)

        val conf = StringMap()
        conf.set("foo", "bar")

        val scheduledFailure = DBScheduledFailure()
                .setInterval("test-interval-1")!!
                .setSapper("noop")!!
                .setSearchTerm("hslogger-app")!!
                .setConfiguration(conf)!!
                .setDuration(30)!!
                .setSapperArgs(StringArray())!!
                .setScheduledAt(42)!!
                .setScheduledBy("abesto")!!
        println("Before save: id=${scheduledFailure.id} ${scheduledFailure.model}")

        mapper.save(scheduledFailure)
        val loadedScheduledFailure = mapper.load(javaClass<DBScheduledFailure>(), scheduledFailure.id)
        println("Read back from DB: id=${loadedScheduledFailure?.id} ${loadedScheduledFailure?.model}")

        val c = DBCharge()
                .setAt(System.currentTimeMillis() / 1000)!!
                .setStatus(ChargeStatus.FUTURE)!!
                .setLog("Test log!")!!
                .setScheduledFailure(scheduledFailure)!!
        println("Before save: id=${c.id} ${c.model}")

        mapper.save(c)
        val loadedCharge = mapper.load(javaClass<DBCharge>(), c.id)
        println("Read back from DB: id=${loadedCharge?.id} ${loadedCharge?.model}")

        println("ScheduledFailure of Charge read back from DB: ${loadedCharge?.getScheduledFailure(mapper)?.model}")
    }
}
