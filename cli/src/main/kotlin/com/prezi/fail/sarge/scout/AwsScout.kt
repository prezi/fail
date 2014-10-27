package com.prezi.fail.sarge.scout

import com.prezi.fail.sarge.Aws
import com.amazonaws.services.ec2.model.Filter
import com.amazonaws.services.ec2.model.DescribeInstancesRequest
import org.slf4j.LoggerFactory
import com.prezi.fail.config.SargeConfig

abstract class AwsScout(val config: SargeConfig) : Scout {
    val logger = LoggerFactory.getLogger(this.javaClass)!!
    open val aws = Aws(config)

    fun availabilityZoneFilter(): List<Filter> {
        val az = config.getAvailabilityZone()
        return when (az) {
            is String -> {
                logger.info("Selecting instances only from availability zone ${az}")
                listOf(Filter("availability-zone", listOf(config.getAvailabilityZone()!!)))
            }
            else -> listOf()
        }
    }

    abstract fun buildFilters(by: String): List<Filter>

    override fun findTargets(by: String) =
            aws.ec2().describeInstances(
                    DescribeInstancesRequest().withFilters(buildFilters(by) + availabilityZoneFilter())
            )
                    ?.getReservations()!!
                    .flatMap{ it.getInstances()!!}
                    .filter{ it.getState()?.getName() == "running" }
                    .map{ it.getPublicDnsName()!!}
}
