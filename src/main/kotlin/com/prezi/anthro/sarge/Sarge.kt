package com.prezi.anthro.sarge

import java.util.Date

import org.slf4j.LoggerFactory

import com.prezi.anthro.sarge.scout.ScoutFactory
import com.prezi.anthro.sarge.mercy.MercyFactory


public class Sarge(val config: SargeConfig = SargeConfig(),
                   scoutFactory: ScoutFactory = ScoutFactory(),
                   mercyFactory: MercyFactory = MercyFactory())
{
    val logger = LoggerFactory.getLogger(this.javaClass)!!
    val scout = scoutFactory.build(config)
    val mercy = mercyFactory.build(config)

    fun charge(tag: String, robot: String, runtime: String) {
        val dir = "/tmp/anthro-${robot}-${Date().getTime()}"
        val remoteTgz = "${dir}/robots.tgz"
        logger.info("${config.getScoutType()} looking for victims by ${tag}...")

        val targets = scout.findTargets(tag)
        logger.info("Found ${targets.size()} targets: ${targets}")

        val deathRow = mercy.spare(targets)
        logger.info("Targets on death row after ${config.getMercyType()}: ${deathRow}")

        deathRow.forEach {
            logger.info("Robot '${robot}' will hammer ${it} for ${runtime} seconds")
            Ssh(it)
                    .exec("echo \$HOSTNAME")
                    .exec("mkdir ${dir}")
                    .put(config.getRobotsTargzPath(), remoteTgz)
                    .exec("cd ${dir} && tar -xzf robots.tgz && ./runner.sh ${robot} ${runtime}")
                    .exec("rm -rf ${dir}")
                    .close()
        }
    }
}