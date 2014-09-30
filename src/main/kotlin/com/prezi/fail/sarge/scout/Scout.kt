package com.prezi.fail.sarge.scout

import com.prezi.fail.sarge.SargeConfig
import com.amazonaws.services.ec2.model.Filter

trait Scout {
    fun findTargets(by: String): List<String>
}