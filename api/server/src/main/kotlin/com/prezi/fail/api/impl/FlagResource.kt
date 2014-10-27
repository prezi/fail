package com.prezi.fail.api.impl

import com.linkedin.restli.server.annotations.RestLiActions
import com.linkedin.restli.server.annotations.ActionParam
import com.prezi.fail.api.db.DB
import com.prezi.fail.api.db.Flag
import com.linkedin.restli.server.annotations.Action


[RestLiActions(namespace = "com.prezi.fail.api", name = "Flag")]
public class FlagResource {
    [Action(name="IsPanic")]
    public fun get(): Boolean = Flag.PANIC.get(DB().mapper)
}
