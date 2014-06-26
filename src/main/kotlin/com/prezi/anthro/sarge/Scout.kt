package com.prezi.anthro.sarge

import com.amazonaws.services.ec2.model.Instance
import com.amazonaws.services.ec2.model.Filter
import com.amazonaws.services.ec2.model.DescribeInstancesRequest

abstract class Scout(val config: SargeConfig){
    val aws = Aws(config)

    abstract fun buildFilters(by: String): List<Filter>

    fun findTargets(by: String): List<Instance> =
            aws.ec2().describeInstances(DescribeInstancesRequest().withFilters(buildFilters(by)))
                    ?.getReservations()!!.flatMap{it.getInstances()!!}
}

