package com.prezi.anthro.sarge

import com.amazonaws.services.ec2.model.DescribeInstancesRequest
import com.amazonaws.services.ec2.model.Filter
import org.slf4j.LoggerFactory
import com.jcraft.jsch.SftpProgressMonitor
import java.util.Date

public class Sarge(val config: SargeConfig = SargeConfig(),
                   scoutFactory: ScoutFactory = ScoutFactory())
{
    val logger = LoggerFactory.getLogger(this.javaClass)!!
    val scout = scoutFactory.build(config)

    fun charge(tag: String, robot: String, runtime: String) {
        val dir = "/tmp/anthro-${robot}-${Date().getTime()}"
        val remoteTgz = "${dir}/robots.tgz"
        logger.info("Looking for victims tagged with ${tag}...")
        scout.findTargets(tag).forEach {
            logger.info("Robot '${robot}' will hammer ${it.getInstanceId()} via public DNS ${it.getPublicDnsName()} for ${runtime} seconds")
            Ssh(it.getPublicDnsName()!!)
                    .exec("echo \$HOSTNAME")
                    .exec("mkdir ${dir}")
                    .put(config.getRobotsTargzPath(), remoteTgz)
                    .exec("cd ${dir} && tar -xzf robots.tgz && ./runner.sh ${robot} ${runtime}")
                    .exec("rm -rf ${dir}")
                    .close()
        }
    }
}