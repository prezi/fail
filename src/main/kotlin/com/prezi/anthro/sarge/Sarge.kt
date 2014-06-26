package com.prezi.anthro.sarge

import com.amazonaws.services.ec2.model.DescribeInstancesRequest
import com.amazonaws.services.ec2.model.Filter
import org.slf4j.LoggerFactory
import com.jcraft.jsch.SftpProgressMonitor

class Sarge(config: SargeConfig = SargeConfig()) {
    val aws = Aws(config)
    val logger = LoggerFactory.getLogger(this.javaClass)!!

    fun hello(tag: String) {
        logger.info("starting run against tag ${tag}")
        aws.ec2().describeInstances(
                DescribeInstancesRequest().withFilters(Filter("tag-key", array(tag).toList()))
        )?.getReservations()?.forEach {
            reservation -> reservation.getInstances()?.forEach {
                instance -> Ssh(instance.getPublicIpAddress()!!)
                .exec("echo hello from \$HOSTNAME")
                .put("/tmp/foo", "/tmp/foo", LoggingSftpProgressMonitor())
                .exec("cat /tmp/foo")
                .close()
            }
        }
    }
}