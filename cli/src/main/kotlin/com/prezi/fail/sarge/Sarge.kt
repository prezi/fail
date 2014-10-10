package com.prezi.fail.sarge

import java.util.Date

import org.slf4j.LoggerFactory

import com.prezi.fail.sarge.scout.ScoutFactory
import com.prezi.fail.sarge.mercy.MercyFactory
import kotlin.concurrent.thread
import com.prezi.changelog.ChangelogClient
import com.prezi.changelog.ChangelogClientConfig
import com.prezi.fail.FailChangelogClientConfig
import com.prezi.fail.cli.CliConfig
import com.prezi.fail.SargeConfig

public class Sarge(val config: SargeConfig = SargeConfig(),
                   scoutFactory: ScoutFactory = ScoutFactory(),
                   mercyFactory: MercyFactory = MercyFactory(),
                   val cliConfig: CliConfig = CliConfig())
{
    val logger = LoggerFactory.getLogger(this.javaClass)!!
    val scout = scoutFactory.build(config)
    val mercy = mercyFactory.build(config)
    val changelog: ChangelogClient? = if (config.useChangelog()) ChangelogClient(FailChangelogClientConfig()) else null

    val user = System.getProperty("user.name")

    fun charge(tag: String, sapper: String, runtime: String, args: List<String> = listOf()) {
        val dir = "/tmp/fail-${sapper}-${Date().getTime()}"
        val remoteTgz = "${dir}/sappers.tgz"
        logger.info("${config.getScoutType()} looking for victims by ${tag}...")

        val targets = scout.findTargets(tag)
        logger.info("Found ${targets.size()} targets: ${targets}")

        val deathRow = mercy.deny(targets)
        logger.info("Targets on death row after ${config.getMercyType()}: ${deathRow}")

        if (cliConfig.isDryRun()) {
            logger.info("Doing dry-run, not starting sappers.")
        } else {
            charge(args, deathRow, dir, remoteTgz, runtime, sapper, user)
        }
    }

    private fun charge(args: List<String>, deathRow: List<String>, dir: String, remoteTgz: String, runtime: String, sapper: String, user: String?) {
        changelog?.send("${user} starting ${sapper} against ${deathRow.join(", ")} for ${runtime} seconds")
        deathRow forEach {
            thread(start = true, block = {
                logger.info("Sapper '${sapper}' will hammer ${it} for ${runtime} seconds")
                val killSwitch = KillSwitch(sapper, it)
                Runtime.getRuntime().addShutdownHook(killSwitch)
                runSapper(args, dir, it, remoteTgz, runtime, sapper)
                Runtime.getRuntime().removeShutdownHook(killSwitch)
            })
        }
    }

    private fun runSapper(args: List<String>, dir: String, it: String, remoteTgz: String, runtime: String, sapper: String) {
        Ssh(it)
                .exec("echo \$HOSTNAME", {this.debug(it)})
                .exec("mkdir ${dir}", {this.debug(it)})
                .put(config.getSappersTargzPath()!!, remoteTgz)
                .exec("cd ${dir} && tar -xzf sappers.tgz && ./runner.sh ${sapper} ${runtime} ${args.join(" ")}", {logger.info(it)} )
                .exec("rm -rf ${dir}", {this.debug(it)})
                .close()
    }

    inner class KillSwitch(val sapper: String, val host: String): Thread() {
        override fun run() {
            logger.info("Terminating '${sapper}' on ${host}")
            Ssh(host).exec("pkill -f ' ./runner.sh '", {this.debug(it)})
            logger.info("Terminated '${sapper}' on ${host}")
            changelog?.send("${user} terminated ${sapper} on ${host}")
        }
    }
}