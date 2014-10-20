package com.prezi.fail.worker

import com.prezi.fail.config.Config

class ImproperlyConfiguredException(msg: String): RuntimeException(msg)

public enum class WorkerConfigKey(val key: String) {
    CLI_EXECUTABLE_PATH : WorkerConfigKey("fail.worker.cliExecutablePath")  // Path to the cli "binary"
    override fun toString(): String = key
}

public class WorkerConfig: Config<WorkerConfigKey>() {
    override fun getToggledValue(key: WorkerConfigKey): String {
        throw UnsupportedOperationException()
    }

    fun getCliExecutablePath(): String = getString(WorkerConfigKey.CLI_EXECUTABLE_PATH)
            ?: throw ImproperlyConfiguredException("${WorkerConfigKey.CLI_EXECUTABLE_PATH} must be set")
}
