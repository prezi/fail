package com.prezi.anthro.sarge

import com.amazonaws.services.ec2.model.DescribeInstancesRequest
import com.amazonaws.services.ec2.model.Filter

class Sarge(config: SargeConfig = SargeConfig()) {
    val aws = Aws(config)
    val ssh = Ssh()

    fun hello(tag: String) {
        aws.ec2().describeInstances(
                DescribeInstancesRequest().withFilters(Filter("tag-key", array(tag).toList()))
        )?.getReservations()?.forEach {
            reservation -> reservation.getInstances()?.forEach {
                instance -> ssh.exec(instance.getPublicIpAddress()!!, "echo hello from \$HOSTNAME")
            }
        }
//        ssh.exec("oam3.us.prezi.private", "echo hello from \$HOSTNAME")
    }
}