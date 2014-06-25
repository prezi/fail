package com.prezi.anthro.sarge

import org.slf4j.LoggerFactory

import com.jcraft.jsch.JSch
import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.agentproxy.RemoteIdentityRepository
import com.jcraft.jsch.Session
import com.jcraft.jsch.agentproxy.ConnectorFactory

import com.prezi.anthro.inHome


class Ssh(val config: SshConfig = SshConfig()) {
    val logger = LoggerFactory.getLogger(this.javaClass)!!

    fun useSshAgent(jsch: JSch, session: Session) {
        logger.debug("using ssh-agent for authentication")
        session.setConfig("PreferredAuthentications", "publickey")
        val sshAgentConnector = ConnectorFactory.getDefault()?.createConnector()
        val sshAgentIdentityRepository = RemoteIdentityRepository(sshAgentConnector)
        jsch.setIdentityRepository(sshAgentIdentityRepository)
    }

    fun exec(host: String, cmd: String) {
        logger.info("executing on ${host}: ${cmd}")

        val jsch = JSch()

        val session = jsch.getSession(host)!!
        if (config.shouldDisableHostKeyChecking()) session.setConfig("StrictHostKeyChecking", "no")
        if (config.getAuthType() == AuthType.SSH_AGENT) useSshAgent(jsch, session)
        session.connect()

        val channel = session.openChannel("exec") as ChannelExec
        val inputStream = channel.getInputStream()!!
        channel.setCommand(cmd)
        channel.connect()

        val reader = inputStream.buffered().reader()
        reader.forEachLine { line -> logger.info("response line: ${line}") }

        channel.disconnect()
        session.disconnect()
    }
}
