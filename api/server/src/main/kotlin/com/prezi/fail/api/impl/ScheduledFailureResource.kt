package com.prezi.fail.api.impl

import com.linkedin.restli.server.resources.CollectionResourceTemplate
import com.prezi.fail.api.ScheduledFailure
import com.linkedin.restli.server.annotations.RestLiCollection
import com.linkedin.restli.server.CreateResponse
import org.slf4j.LoggerFactory
import com.linkedin.restli.server.PagingContext
import com.linkedin.restli.server.annotations.Context
import com.linkedin.restli.server.annotations.Action
import com.linkedin.data.template.LongMap

[RestLiCollection(name="ScheduledFailure", namespace="com.prezi.fail.api")]
public class ScheduledFailureResource : CollectionResourceTemplate<String, ScheduledFailure>() {
    val logger = LoggerFactory.getLogger(javaClass)!!

    override public fun create(entity: ScheduledFailure?): CreateResponse {
        logger.info("Scheduling ${entity}")
        return CreateResponse(42)
    }

    override fun getAll([Context] pagingContext: PagingContext?): MutableList<ScheduledFailure>? {
        logger.info("Listing all scheduled jobs")
        return arrayListOf(
                ScheduledFailure().setSapper("example1")!!,
                ScheduledFailure().setSapper("example2")!!
        )
    }
}