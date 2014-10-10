package com.prezi.fail.sarge.scout

import com.amazonaws.services.ec2.model.Filter

trait Scout {
    fun findTargets(by: String): List<String>
}