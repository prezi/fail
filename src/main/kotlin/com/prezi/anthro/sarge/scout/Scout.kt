package com.prezi.anthro.sarge.scout

import com.prezi.anthro.sarge.SargeConfig
import com.amazonaws.services.ec2.model.Filter

abstract class Scout(val config: SargeConfig){
    abstract fun findTargets(by: String): List<String>
}