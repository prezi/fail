package com.prezi.fail.api.impl

import com.linkedin.restli.server.annotations.RestLiCollection
import com.linkedin.restli.server.resources.CollectionResourceTemplate
import java.util.HashMap
import com.prezi.fail.api.Healthcheck

[RestLiCollection(name="healthcheck", namespace="com.prezi.fail")]
public class FortunesResource: CollectionResourceTemplate<Long, Healthcheck>()
{
    override public fun get(key: Long?): Healthcheck?
    {
        return Healthcheck().setOk("Such OK!")
    }
}
