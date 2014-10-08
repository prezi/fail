package com.prezi.fail.api.impl

import com.linkedin.restli.server.resources.CollectionResourceTemplate
import com.prezi.fail.api.ScheduledFailure
import com.linkedin.restli.server.annotations.RestLiCollection

[RestLiCollection(name="ScheduledFailure", namespace="com.prezi.fail.api")]
public class ScheduledFailureResource : CollectionResourceTemplate<Long, ScheduledFailure>() {
}