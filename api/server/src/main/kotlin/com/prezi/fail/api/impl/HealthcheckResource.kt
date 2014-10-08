package com.prezi.fail.api.impl

import com.prezi.fail.api.Healthcheck
import com.linkedin.restli.server.annotations.RestLiSimpleResource
import com.linkedin.restli.server.resources.SimpleResourceTemplate

[RestLiSimpleResource(name="healthcheck", namespace="com.prezi.fail")]
public class HealthcheckResource: SimpleResourceTemplate<Healthcheck>()
{
    override public fun get(): Healthcheck?
    {
        return Healthcheck().setRunning(true)
    }
}
