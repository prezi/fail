package com.prezi.fail.api.impl

import com.linkedin.restli.server.resources.CollectionResourceTemplate
import com.prezi.fail.api.ScheduledFailure
import com.linkedin.restli.server.annotations.RestLiCollection
import com.linkedin.restli.server.CreateResponse
import org.slf4j.LoggerFactory

[RestLiCollection(name="ScheduledFailure", namespace="com.prezi.fail.api")]
public class ScheduledFailureResource : CollectionResourceTemplate<Long, ScheduledFailure>() {
    val logger = LoggerFactory.getLogger(javaClass)!!

    override public fun create(entity: ScheduledFailure?): CreateResponse {
        logger.info("Scheduling ${entity}")
        return CreateResponse(42)
    }
}