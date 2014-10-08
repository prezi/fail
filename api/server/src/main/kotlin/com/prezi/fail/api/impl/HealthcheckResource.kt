package com.prezi.fail.api.impl

import com.linkedin.restli.server.annotations.RestLiSimpleResource
import com.linkedin.restli.server.resources.SimpleResourceTemplate
import com.prezi.fail.api.Healthcheck
import com.linkedin.restli.server.annotations.RestMethod

[RestLiSimpleResource(name="Healthcheck", namespace="com.prezi.fail.api")]
public class HealthcheckResource : SimpleResourceTemplate<Healthcheck>()
{
    override public fun get(): Healthcheck?
    {
        return Healthcheck().setRunning(true)
    }
}
