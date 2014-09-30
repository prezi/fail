package com.prezi.fail.sarge

import java.util.Date

import org.slf4j.LoggerFactory

import com.prezi.fail.sarge.scout.ScoutFactory
import com.prezi.fail.sarge.mercy.MercyFactory
import kotlin.concurrent.thread
import com.prezi.changelog.ChangelogClient
import com.prezi.changelog.ChangelogClientConfig
import com.prezi.fail.FailChangelogClientConfig

public class Sarge(val config: SargeConfig = SargeConfig(),
                   scoutFactory: ScoutFactory = ScoutFactory(),
                   mercyFactory: MercyFactory = MercyFactory())
{
    val logger = LoggerFactory.getLogger(this.javaClass)!!
    val scout = scoutFactory.build(config)
    val mercy = mercyFactory.build(config)
    val changelog: ChangelogClient? = if (config.useChangelog()) ChangelogClient(FailChangelogClientConfig()) else null


    fun charge(tag: String, sapper: String, runtime: String, args: List<String> = listOf()) {
        val dir = "/tmp/fail-${sapper}-${Date().getTime()}"
        val remoteTgz = "${dir}/sappers.tgz"
        logger.info("${config.getScoutType()} looking for victims by ${tag}...")

        val targets = scout.findTargets(tag)
        logger.info("Found ${targets.size()} targets: ${targets}")

        val deathRow = mercy.deny(targets)
        logger.info("Targets on death row after ${config.getMercyType()}: ${deathRow}")

        val user = System.getProperty("user.name")
        changelog?.send("${user} starting ${sapper} against ${deathRow.join(", ")} for ${runtime} seconds")
        deathRow forEach { thread(start = true, block = {
            logger.info("Sapper '${sapper}' will hammer ${it} for ${runtime} seconds")
            val killSwitch = object : Thread() {
                override fun run() {
                    logger.info("Terminating '${sapper}' on ${it}")
                    Ssh(it).exec("pkill -f ' ./runner.sh '")
                    logger.info("Terminated '${sapper}' on ${it}")
                    changelog?.send("${user} terminated ${sapper} on ${it}")
                }
            }
            Runtime.getRuntime().addShutdownHook(killSwitch)
            Ssh(it)
                    .exec("echo \$HOSTNAME")
                    .exec("mkdir ${dir}")
                    .put(config.getSappersTargzPath(), remoteTgz)
                    .exec("cd ${dir} && tar -xzf sappers.tgz && ./runner.sh ${sapper} ${runtime} ${args.join(" ")}")
                    .exec("rm -rf ${dir}")
                    .close()
            Runtime.getRuntime().removeShutdownHook(killSwitch)
        })
        }
    }
}