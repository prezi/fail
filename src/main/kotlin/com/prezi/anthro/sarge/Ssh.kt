package com.prezi.anthro.sarge

import org.slf4j.LoggerFactory

import com.jcraft.jsch.JSch
import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.agentproxy.RemoteIdentityRepository
import com.jcraft.jsch.Session
import com.jcraft.jsch.agentproxy.ConnectorFactory

import com.prezi.anthro.inHome
import kotlin.properties.Delegates
import java.io.InputStream
import java.io.OutputStream


class Ssh(val host: String, val config: SshConfig = SshConfig()) {
    val logger = LoggerFactory.getLogger(this.javaClass)!!
    val session: Session by Delegates.lazy {
        val jsch = JSch()
        val session = jsch.getSession(host)!!
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

    fun overExecChannel(cmd: String, f: (InputStream, OutputStream) -> Unit): Ssh {
        val channel = session.openChannel("exec") as ChannelExec
        val input = channel.getInputStream()!!
        val output = channel.getOutputStream()!!
        channel.setCommand(cmd)
        channel.connect()
        f(input, output)
        channel.disconnect()
        return this
    }

    fun exec(cmd: String): Ssh {
        logger.info("executing on ${host}: ${cmd}")

        return overExecChannel(cmd, { input, output ->
            val reader = input.buffered().reader()
            reader.forEachLine { line -> logger.info("response line: ${line}") }
        })
    }

    fun close() {
        session.disconnect()
    }
}
