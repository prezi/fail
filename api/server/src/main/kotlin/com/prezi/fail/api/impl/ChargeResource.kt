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

[RestLiCollection(name="Charge", namespace="com.prezi.fail.api")]
public class ChargeResource : CollectionResourceTemplate<Long, Charge>() {
    val logger = LoggerFactory.getLogger(javaClass)!!

    [Finder("time")]
    public fun listChargesByTime(
            [Context] paging: PagingContext,
            [QueryParam("at")] at: Long,
            [Optional][QueryParam("before")] before: String,
            [Optional][QueryParam("after")] after: String,
            [Optional][QueryParam("context")] context: String
    ): List<Charge> {
        logger.info("Listing scheduled jobs at=${at} before=${before} after=${after} context=${context}")
        return listOf(
                Charge().setLog("example1 log")?.setScheduledFailure(ScheduledFailure().setSapper("example1"))!!,
                Charge().setLog("example2 log")?.setScheduledFailure(ScheduledFailure().setSapper("example2"))!!
        )
    }
}

