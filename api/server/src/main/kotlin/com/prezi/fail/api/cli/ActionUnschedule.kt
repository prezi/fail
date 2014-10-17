package com.prezi.fail.api.cli

import com.prezi.fail.api.ScheduledFailure
import com.linkedin.data.template.StringArray
import com.linkedin.data.template.StringMap
import com.prezi.fail.cli.Action
import org.slf4j.LoggerFactory
import com.prezi.fail.api.period.PeriodFactory
import com.prezi.fail.api.db.DBScheduledFailure
import com.prezi.fail.api.db.DB
import org.joda.time.DateTime
import com.prezi.fail.api.db.DBRun
import com.prezi.fail.api.extensions.nextRun
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression
import com.amazonaws.services.dynamodbv2.model.Condition
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator
import org.joda.time.DateTimeConstants
import com.prezi.fail.api.RunStatus

public class ActionUnschedule(val args: Array<String>, systemProperties: StringMap) : Action() {
    val logger = LoggerFactory.getLogger(javaClass)!!
    val config = ApiCliConfig().withConfigMap(systemProperties) as ApiCliConfig

    class object {
        val requiredArgCount = 1
        val verb = "unschedule"
        val cmdLineSyntax = "${verb} id"
    }

    override public fun run() {
        val config = ApiCliConfig()

        val id = args[0]
        logger.debug("Handling delete request for runs of ScheduledFailure ${id}")

        val dbScheduledFailure = DB.mapper.load(javaClass<DBScheduledFailure>(), id)
        if (dbScheduledFailure == null) {
            logger.warn("No scheduled failure found with id ${id}. You can use `${ActionList.cmdLineSyntax}` to find the IDs")
            return
        }

        val runs = DB.mapper.query(
                javaClass<DBRun>(),
                DynamoDBQueryExpression<DBRun>()
                    .withIndexName("ScheduledFailureId-At-index")
                    .withHashKeyValues(DBRun().setScheduledFailureId(id)?.setId("this-is-unused"))
                    .withConsistentRead(false)  // Consistent reads are not supported on global secondary indexes
        )
        logger.debug("Found ${runs.size} runs")

        if (config.isDryRun()) {
            logger.info("Collected schedule and ${runs.size} runs, not deleting anything because this is a dry-run")
        } else {
            DB.mapper.batchDelete(runs)
            logger.info("Deleted ${runs.size} runs: ${runs}")

            DB.mapper.delete(dbScheduledFailure)
            logger.info("Deleted schedule: ${dbScheduledFailure}")
        }
    }
}
