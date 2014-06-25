package com.prezi.anthro.sarge

import com.jcraft.jsch.JSch
import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.agentproxy.RemoteIdentityRepository
import com.jcraft.jsch.Session
import com.jcraft.jsch.agentproxy.ConnectorFactory

import com.prezi.anthro.inHome


class Ssh(val config: SshConfig = SshConfig()) {
    fun useSshAgent(jsch: JSch, session: Session) {
        session.setConfig("PreferredAuthentications", "publickey")
        val sshAgentConnector = ConnectorFactory.getDefault()?.createConnector()
        val sshAgentIdentityRepository = RemoteIdentityRepository(sshAgentConnector)
        jsch.setIdentityRepository(sshAgentIdentityRepository)
    }

    fun exec(host: String, cmd: String) {
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
        reader.forEachLine { line -> println(line) }

        channel.disconnect()
        session.disconnect()
    }
}
