package com.prezi.fail.api.impl

import com.linkedin.restli.server.resources.CollectionResourceTemplate
import com.prezi.fail.api.ScheduledFailure
import com.linkedin.restli.server.annotations.RestLiCollection
import org.slf4j.LoggerFactory
import com.linkedin.restli.server.PagingContext
import com.linkedin.restli.server.annotations.Context
import com.linkedin.restli.server.annotations.Finder
import com.linkedin.restli.server.annotations.QueryParam
import com.linkedin.restli.server.annotations.Optional
import com.prezi.fail.api.Run
import com.prezi.fail.api.db.DB
import com.prezi.fail.api.db.DBRun
import com.amazonaws.services.dynamodbv2.model.Condition
import org.joda.time.DateTime
import org.joda.time.Interval
import org.joda.time.DateTimeConstants
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression
import com.prezi.fail.api.db.DBScheduledFailure
import com.prezi.fail.api.extensions.*
import com.amazonaws.services.dynamodbv2.datamodeling.KeyPair
import java.util.HashMap
import java.util.LinkedList

[RestLiCollection(name="Run", namespace="com.prezi.fail.api")]
public class RunResource : CollectionResourceTemplate<String, Run>() {
    val logger = LoggerFactory.getLogger(javaClass)!!

    [Finder("time")]
    public fun listRunsByTime(
            [QueryParam("at")] atTimestamp: Long,
            [Optional][QueryParam("before")] secondsBefore: Int?,
            [Optional][QueryParam("after")] secondsAfter: Int?,
            [Optional][QueryParam("context")] secondsContext: Int?
    ): List<Run> {
        val at = DateTime(atTimestamp * 1000)
        val interval = Interval(
                at.minusSeconds(secondsBefore ?: secondsContext ?: 0),
                at.plusSeconds(secondsAfter ?: secondsContext ?: 3 * DateTimeConstants.SECONDS_PER_HOUR))
        logger.trace("Listing scheduled runs for parameters at=${at} before=${secondsBefore} after=${secondsAfter} context=${secondsContext}")
        logger.info("Scheduled runs in interval ${interval}")

        val runsFromDb = loadRunsBetween(interval)
        logger.debug("Loaded already existing runs from db: ${runsFromDb}")

        populateScheduledFailuresIntoRuns(runsFromDb)

        val scheduledFailures = loadAllScheduledFailures()
        logger.trace("Loaded all scheduled failures: ${scheduledFailures}")

        val additionalRuns = generateAdditionalRuns(interval, runsFromDb, scheduledFailures)
        DB.mapper.batchSave(additionalRuns)
        additionalRuns.forEach { logger.debug("Scheduled new run: ${it}")}

        return (runsFromDb + additionalRuns).map{it.model}.sortBy{it.getAt()}
    }

    private fun loadRunsBetween(interval: Interval): List<DBRun> {
        val condition = Condition()
                .withComparisonOperator(ComparisonOperator.BETWEEN)
                .withAttributeValueList(
                        AttributeValue().withN(
                                (interval.getStart().getMillis() / 1000).toString()),
                        AttributeValue().withN(
                                (interval.getEnd().getMillis() / 1000).toString()))
        val scanExp = DynamoDBScanExpression()
        scanExp.addFilterCondition("At", condition)
        return DB.mapper.scan(javaClass<DBRun>(), scanExp).toList()
    }

    fun loadAllScheduledFailures(): List<DBScheduledFailure> =
        DB.mapper.scan(javaClass<DBScheduledFailure>(), DynamoDBScanExpression()).toList()

    fun generateAdditionalRuns(interval: Interval, runsFromDb: List<DBRun>, scheduledFailures: List<DBScheduledFailure>): List<DBRun> =
        scheduledFailures.flatMap { scheduledFailure ->
            scheduledFailure.model.nextRuns(
                    interval.withStartMillis(
                        runsFromDb
                                .filter{it.getScheduledFailureId() == scheduledFailure.id}
                                .maxBy{it.getAt()!!}
                                ?.getAtMillis() ?: interval.getStart().getMillis()
                    )
            ).map{ DBRun(it).setScheduledFailureId(scheduledFailure.id)!!}
        }

    fun populateScheduledFailuresIntoRuns(runs: List<DBRun>) {
        if (runs.empty) {
            logger.trace("No runs loaded from DB, not populating with ScheduledFailure data")
            return
        }
        val batchLoadResult = DB.mapper.batchLoad(
                runs.map{it.getScheduledFailureId()!!}.toSet().map{
                    DBScheduledFailure().withId(it)!!
                }
        )
        // We know we've selected from only a single table
        val dbScheduledFailures =
                (batchLoadResult.get(batchLoadResult.keySet().first()) as List<DBScheduledFailure>)
                        .toMap{it.id}
        logger.trace("Loaded DBScheduledFailures to populate runs: ${dbScheduledFailures}")
        runs.forEach { it.model.setScheduledFailure(dbScheduledFailures.get(it.getScheduledFailureId())?.model) }
    }

    [Finder("timeAndRegex")]
    public fun listRunsByTimeAndRegex(
            [Context] paging: PagingContext,
            [QueryParam("regex")] regex: String,
            [QueryParam("at")] at: Long,
            [Optional][QueryParam("before")] before: String,
            [Optional][QueryParam("after")] after: String,
            [Optional][QueryParam("context")] context: String
    ): List<Run> {
        logger.info("Listing scheduled jobs at=${at} before=${before} after=${after} context=${context} regex=${regex}")
        return listOf(
                Run().setLog("example1 log")?.setScheduledFailure(ScheduledFailure().setSapper("example1"))!!,
                Run().setLog("example2 log")?.setScheduledFailure(ScheduledFailure().setSapper("example2"))!!
        )
    }
}

