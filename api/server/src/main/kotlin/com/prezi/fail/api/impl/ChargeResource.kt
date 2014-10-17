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
import com.prezi.fail.api.Charge
import com.prezi.fail.api.db.DB
import com.prezi.fail.api.db.DBCharge
import com.amazonaws.services.dynamodbv2.model.Condition
import org.joda.time.DateTime
import org.joda.time.Interval
import org.joda.time.DateTimeConstants
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression
import com.prezi.fail.api.db.DBScheduledFailure
import com.prezi.fail.api.extensions.*

[RestLiCollection(name="Charge", namespace="com.prezi.fail.api")]
public class ChargeResource : CollectionResourceTemplate<String, Charge>() {
    val logger = LoggerFactory.getLogger(javaClass)!!

    [Finder("time")]
    public fun listChargesByTime(
            [Context] paging: PagingContext,
            [QueryParam("at")] atTimestamp: Long,
            [Optional][QueryParam("before")] secondsBefore: Int?,
            [Optional][QueryParam("after")] secondsAfter: Int?,
            [Optional][QueryParam("context")] secondsContext: Int?
    ): List<Charge> {
        val at = DateTime(atTimestamp * 1000)
        val interval = Interval(
                at.minusSeconds(secondsBefore ?: secondsContext ?: 0),
                at.plusSeconds(secondsAfter ?: secondsContext ?: 3 * DateTimeConstants.SECONDS_PER_HOUR))
        logger.info("Listing scheduled jobs for parameters at=${at} before=${secondsBefore} after=${secondsAfter} context=${secondsContext} " +
        "parsed to interval ${interval}")

        val runsFromDb = loadRunsBetween(interval)
        val scheduledFailures = loadAllScheduledFailures()
        val additionalRuns = generateAdditionalRuns(interval, runsFromDb, scheduledFailures)

        DB.mapper.batchSave(additionalRuns)
        return (runsFromDb + additionalRuns).map{it.model}
    }

    private fun loadRunsBetween(interval: Interval): List<DBCharge> {
        val condition = Condition()
                .withComparisonOperator(ComparisonOperator.BETWEEN)
                .withAttributeValueList(
                        AttributeValue().withN(
                                (interval.getStart().getMillis() / 1000).toString()),
                        AttributeValue().withN(
                                (interval.getEnd().getMillis() / 1000).toString()))

        val scanExp = DynamoDBScanExpression()
        scanExp.addFilterCondition("At", condition)
        return DB.mapper.scan(javaClass<DBCharge>(), scanExp).toList()
    }

    fun loadAllScheduledFailures(): List<DBScheduledFailure> =
        DB.mapper.scan(javaClass<DBScheduledFailure>(), DynamoDBScanExpression()).toList()

    fun generateAdditionalRuns(interval: Interval, runsFromDb: List<DBCharge>, scheduledFailures: List<DBScheduledFailure>): List<DBCharge> =
        scheduledFailures.flatMap { scheduledFailure ->
            scheduledFailure.model.nextRuns(
                    interval.withStartMillis(
                        runsFromDb
                                .filter{it.getScheduledFailureId() == scheduledFailure.id}
                                .maxBy{it.getAt()!!}
                                ?.getAtMillis() ?: interval.getStart().getMillis()
                    )
            ).map{DBCharge(it)}
        }

    [Finder("timeAndRegex")]
    public fun listChargesByTimeAndRegex(
            [Context] paging: PagingContext,
            [QueryParam("regex")] regex: String,
            [QueryParam("at")] at: Long,
            [Optional][QueryParam("before")] before: String,
            [Optional][QueryParam("after")] after: String,
            [Optional][QueryParam("context")] context: String
    ): List<Charge> {
        logger.info("Listing scheduled jobs at=${at} before=${before} after=${after} context=${context} regex=${regex}")
        return listOf(
                Charge().setLog("example1 log")?.setScheduledFailure(ScheduledFailure().setSapper("example1"))!!,
                Charge().setLog("example2 log")?.setScheduledFailure(ScheduledFailure().setSapper("example2"))!!
        )
    }
}

