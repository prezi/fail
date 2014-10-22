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
import org.apache.commons.exec.environment.EnvironmentUtils
import com.prezi.fail.logging.LogCollector
import com.prezi.fail.api.Api
import com.prezi.fail.api.RunBuilders
import com.linkedin.restli.client.util.PatchGenerator
import com.prezi.fail.api.RunStatus

public class Worker(val queue: Queue = Queue(), val api: Api = Api()) {
    val logger = LoggerFactory.getLogger(javaClass)!!

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
                                 "\n\nOUTPUT:\n${output}")
                                 System.exit(1)
                }
         ).waitFor()
    }

    fun performFailureInjectionRun(run: Run) {
        val logCollector = LogCollector().start()
        val patch = Run()
        runCli(
                args = (array("once",
                        run.getScheduledFailure().getSearchTerm(),
                        run.getScheduledFailure().getSapper(),
                        run.getScheduledFailure().getDuration().toString()
                ) + run.getScheduledFailure().getSapperArgs()).copyToArray(),
                javaOpts = run.getScheduledFailure().getConfiguration(),
                callback = {
                    patch.setStatus(RunStatus.DONE)
                    logger.info("${run} finished\n${output}")
                },
                error = {
                    patch.setStatus(RunStatus.FAILED)
                    logger.error("${run} failed\n${output}", it)
                },
                finally = {
                    patch.setLog(logCollector.stopAndGetEncodedMessages())
                    val request = RunBuilders()
                            .partialUpdate()
                            .id(run.getId())
                            .input(PatchGenerator.diffEmpty(patch))
                    api.authenticate(request)
                    api.sendRequest(request.build())
                }
        )
        logger.info("Starting ${run}")
    }

    class ResultHandler(val cmd: CommandLine,
                        val executor: Executor,
                        val output: OutputStream,
                        val cb: ResultHandler.(Int) -> Unit,
                        val error: ResultHandler.(ExecuteException) -> Unit,
                        val finally: ResultHandler.() -> Unit)
    : DefaultExecuteResultHandler() {
        override fun onProcessComplete(exitValue: Int) {
            super.onProcessComplete(exitValue)
            cb(exitValue)
            finally()
        }
        override fun onProcessFailed(e: ExecuteException?) {
            super.onProcessFailed(e)
            error(e!!)
            finally()
        }
    }

    fun runCli(args: Array<String>,
               javaOpts: Map<String, String> = mapOf(),
               configure: ResultHandler.() -> Unit = {},
               callback: ResultHandler.(Int) -> Unit = {},
               error: ResultHandler.(ExecuteException) -> Unit = {},
               finally: ResultHandler.() -> Unit = {}): ResultHandler {
        val cmd = CommandLine(cliExecutablePath)
        cmd.addArguments(args)

        val output = ByteArrayOutputStream()
        val streamHandler = PumpStreamHandler(output)

        val executor = DefaultExecutor()
        executor.setStreamHandler(streamHandler)

        val finalEnv: MutableMap<String, String> = hashMapOf()
        finalEnv.putAll(System.getenv())
        finalEnv.put("JAVA_OPTS", (finalEnv.get("JAVA_OPTS") ?: "") + " " + javaOpts.map{"-D${it.key}=${it.value}"}.join(" "))

        val resultHandler = ResultHandler(cmd, executor, output, callback, error, finally)
        resultHandler.configure()

        logger.debug("Starting ${cmd} with env ${finalEnv}")
        executor.execute(cmd, finalEnv, resultHandler)
        return resultHandler
    }
}