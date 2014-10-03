package com.prezi.fail.sarge

// Java stdlib
import java.io.InputStream
import java.io.OutputStream
// Kotlin stdlib
import kotlin.properties.Delegates
// SLF4J
import org.slf4j.LoggerFactory
// JSch
import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session
import com.jcraft.jsch.Channel
import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.ChannelSftp
import com.jcraft.jsch.SftpProgressMonitor
import com.jcraft.jsch.agentproxy.ConnectorFactory
import com.jcraft.jsch.agentproxy.RemoteIdentityRepository
// Fail
import com.prezi.fail.inHome
import org.slf4j.Logger


class Ssh(val host: String, val config: SshConfig = SshConfig()) {
    val logger = LoggerFactory.getLogger(this.javaClass)!!
    val session: Session by Delegates.lazy {
        val jsch = JSch()
        val session = jsch.getSession("root", host)!!
        if (config.shouldDisableHostKeyChecking() ) session.setConfig("StrictHostKeyChecking", "no")
        if (config.getAuthType() == AuthType.SSH_AGENT ) useSshAgent(jsch, session)
        session.connect()
        session
    }

    fun useSshAgent(jsch: JSch, session: Session) {
        logger.debug("using ssh-agent for authentication")
        session.setConfig("PreferredAuthentications", "publickey")
        val sshAgentConnector = ConnectorFactory.getDefault()?.createConnector()
        val sshAgentIdentityRepository = RemoteIdentityRepository(sshAgentConnector)
        jsch.setIdentityRepository(sshAgentIdentityRepository)
    }

    fun overChannel<T : Channel>(channelName: String, beforeConnect: ((T) -> Unit)?, f: (T) -> Unit): Ssh {
        [suppress("UNCHECKED_CAST")] val channel = session.openChannel(channelName) as T
        if (beforeConnect != null) beforeConnect(channel)
        channel.connect()
        f(channel)
        channel.disconnect()
        return this
    }

    fun overExecChannel(cmd: String, f: (InputStream, OutputStream, ChannelExec) -> Unit): Ssh =
            overChannel<ChannelExec>(
                    "exec",
                    {c -> c.setCommand(cmd)},
                    {c -> f(c.getInputStream()!!, c.getOutputStream()!!, c)})

    fun overSftpChannel(f: (ChannelSftp) -> Unit): Ssh = overChannel("sftp", null, f)

    fun exec(cmd: String, level: Logger.(String) -> Unit): Ssh {
        logger.level("${host} < ${cmd}")
        return overExecChannel(cmd, { input, output, channel ->
            val reader = input.buffered().reader()
            reader.forEachLine { line -> logger.info("${host} > ${line}") }
        })
    }

    fun put(src: String, dst: String, monitor: SftpProgressMonitor = LoggingSftpProgressMonitor(host)): Ssh {
        return overSftpChannel({ it.put(src, dst, monitor) })
    }

    fun close() {
        session.disconnect()
    }
}
