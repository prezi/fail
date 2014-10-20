package com.prezi.fail.api.impl

import org.junit.Test
import org.mockito.Matchers.*
import com.prezi.fail.test.givenAny
import com.prezi.fail.api.db.DB
import com.prezi.fail.api.Run
import com.prezi.fail.api.RunStatus
import com.prezi.fail.api.ScheduledFailure
import com.prezi.fail.test.When
import kotlin.test.assertEquals
import com.prezi.fail.api.db.DBRun
import com.prezi.fail.api.db.DBScheduledFailure
import org.junit.Ignore
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB

class AnyDBRun : DBRun() {
    override fun equals(other: Any?): Boolean {
        return true
    }
}

class AnyDBScheduledFailure : DBScheduledFailure() {
    override fun equals(other: Any?): Boolean {
        return true
    }
}

class MyMapper(val dbrun: DBRun, val scheduled: DBScheduledFailure, dynDb: AmazonDynamoDB) : DynamoDBMapper(dynDb) {
    override fun <T> load(keyObject: T?): T? {
        when (keyObject) {
            is DBRun -> { return dbrun as T }
            else -> { return super<DynamoDBMapper>.load(keyObject)}
        }
    }

    override fun batchLoad(itemsToGet: MutableList<Any>?): MutableMap<String, MutableList<Any>>? {
        when(itemsToGet?.first) {
            is DBScheduledFailure -> { return hashMapOf(Pair<String, MutableList<Any>>("", linkedListOf(scheduled))) }
            else -> {return super<DynamoDBMapper>.batchLoad(itemsToGet)}
        }
    }
}

class RunResourceTest {
    Test fun getPopulatesScheduledFailure() {
        val db = givenAny(javaClass<DB>())
        val dynDb = givenAny(javaClass<AmazonDynamoDB>())
        val resource = RunResource(db)
        val scheduled = DBScheduledFailure().setId("a")!!
        val dbrun = DBRun().setId("1")?.setAt(0)?.setLog("testlog")?.setStatus(RunStatus.DONE)?.setScheduledFailureId(scheduled.getId())!!
        When(db.mapper).thenReturn(MyMapper(dbrun, scheduled, dynDb))
        val run = resource.get(dbrun.getId())
        assertEquals(scheduled.model, run?.getScheduledFailure())
    }
}