package com.prezi.fail.api.impl

import com.linkedin.restli.server.resources.CollectionResourceTemplate
import com.prezi.fail.api.ScheduledFailure
import com.linkedin.restli.server.annotations.RestLiCollection
import com.linkedin.restli.server.CreateResponse
import org.slf4j.LoggerFactory
import com.linkedin.restli.server.PagingContext
import com.linkedin.restli.server.annotations.Context
import com.linkedin.restli.server.annotations.Finder
import com.linkedin.restli.server.annotations.QueryParam
import com.linkedin.restli.server.annotations.Optional

[RestLiCollection(name="ScheduledFailure", namespace="com.prezi.fail.api")]
public class ScheduledFailureResource : CollectionResourceTemplate<Long, ScheduledFailure>() {
    val logger = LoggerFactory.getLogger(javaClass)!!

    override public fun create(entity: ScheduledFailure?): CreateResponse {
        logger.info("Scheduling ${entity}")
        return CreateResponse(42)
    }

    [Finder("time")]
    public fun listJobsByTime(
            [Context] paging: PagingContext,
            [QueryParam("at")] at: Long,
            [Optional][QueryParam("before")] before: String,
            [Optional][QueryParam("after")] after: String,
            [Optional][QueryParam("context")] context: String
    ): List<ScheduledFailure> {
        logger.info("Listing scheduled jobs at=${at} before=${before} after=${after} context=${context}")
        return listOf(
            ScheduledFailure().setSapper("example1")!!,
            ScheduledFailure().setSapper("example2")!!
        )
    }
}