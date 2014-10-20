package com.prezi.fail.worker

import org.slf4j.LoggerFactory
import com.prezi.fail.api.Run
import java.io.File
import org.apache.commons.io.IOUtils

public class Worker {
    val logger = LoggerFactory.getLogger(javaClass)!!
    val queue = Queue()

    val cliExecutablePath = WorkerConfig().getCliExecutablePath()

    public fun run() {
        ensureCliBinaryExists()
        ensureCliBinaryWorks()
        while(true) {
            queue.receiveRunAnd { performFailureInjectionRun(it) }
        }
    }


    fun ensureCliBinaryExists() {
        if (!File(cliExecutablePath).canExecute()) {
            logger.error("CLI executable at ${cliExecutablePath} doesn't exist or is not executable.")
            System.exit(1)
        }
    }

    fun ensureCliBinaryWorks() {
        val result = runCli("--help")
        if (result.exitValue() != 0) {
            logger.error("${cliExecutablePath} --help exit with code ${result.exitValue()}, meaning CLI is not healthy, bailing out."+
            "\n\nSTDOUT:\n${IOUtils.toString(result.getInputStream())}" +
            "\n\nSTDERR:\n${IOUtils.toString(result.getErrorStream())}")
            System.exit(1)
        }
    }

    fun performFailureInjectionRun(run: Run) {
        logger.info("This is where I'd perform ${run}")
    }

    fun runCli(vararg args: String): Process {
        val pb = ProcessBuilder(cliExecutablePath, *args)
        val p = pb.start()
        p.waitFor()
        return p
    }
}