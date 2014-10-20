package com.prezi.fail.worker

import org.slf4j.LoggerFactory
import com.prezi.fail.api.Run
import java.io.File
import org.apache.commons.io.IOUtils
import org.apache.commons.exec.CommandLine
import org.apache.commons.exec.DefaultExecutor
import org.apache.commons.exec.Executor
import org.apache.commons.exec.PumpStreamHandler
import org.apache.commons.io.output.ByteArrayOutputStream
import org.apache.commons.exec.ExecuteException
import org.apache.commons.exec.ExecuteResultHandler
import org.apache.commons.exec.DefaultExecuteResultHandler
import java.io.OutputStream

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
        runCli(
                args = array("--help"),
                callback = { logger.info("CLI executable works.") },
                error = {
                    logger.error("${cliExecutablePath} --help exit with code ${getExitValue()}, meaning CLI is not healthy, bailing out."+
                                 "\n\nSTDOUT:\n${stdout}" +
                                 "\n\nSTDERR:\n${stderr}")
                                 System.exit(1)
                }
         ).waitFor()
    }

    fun performFailureInjectionRun(run: Run) {
        logger.info("This is where I'd perform ${run}")
    }

    class ResultHandler(val cmd: CommandLine,
                        val executor: Executor,
                        val stdout: OutputStream,
                        val stderr: OutputStream,
                        val cb: ResultHandler.(Int) -> Unit,
                        val error: ResultHandler.(ExecuteException) -> Unit)
    : DefaultExecuteResultHandler() {
        override fun onProcessComplete(exitValue: Int) {
            super.onProcessComplete(exitValue)
            cb(exitValue)
        }
        override fun onProcessFailed(e: ExecuteException?) {
            super.onProcessFailed(e)
            error(e!!)
        }
    }

    fun runCli(args: Array<String>, configure: (CommandLine) -> Unit = {}, callback: ResultHandler.(Int) -> Unit = {}, error: ResultHandler.(ExecuteException) -> Unit = {}): ResultHandler {
        val cmd = CommandLine(cliExecutablePath)
        cmd.addArguments(args)
        configure(cmd)
        val executor = DefaultExecutor()
        val stdout = ByteArrayOutputStream()
        val stderr = ByteArrayOutputStream()
        val streamHandler = PumpStreamHandler(stdout, stderr)
        executor.setStreamHandler(streamHandler)
        val resultHandler = ResultHandler(cmd, executor, stdout, stderr, callback, error)
        executor.execute(cmd, resultHandler)
        return resultHandler
    }
}