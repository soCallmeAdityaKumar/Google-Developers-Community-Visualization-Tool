package com.example.googledeveloperscommunityvisualisationtool.Connection

import android.util.Log
import com.example.googledeveloperscommunityvisualisationtool.create.utility.model.ActionBuildCommandUtility
import com.jcraft.jsch.ChannelSftp
import com.jcraft.jsch.JSch
import com.jcraft.jsch.JSchException
import com.jcraft.jsch.SftpException
import java.util.Properties

class LGConnectionSendFile private constructor() : Runnable {

    private var user = "lg"
    private var password = "1234"
    private var hostname = "192.168.20.28"
    private var port = 22
    private var filePath: String? = null
    fun setData(user: String, password: String, hostname: String, port: Int) {
        this.user = user
        this.password = password
        this.hostname = hostname
        this.port = port
    }

    fun startConnection() {
        Thread(instance).start()
    }

    override fun run() {
        if (filePath != null) sendFile()
    }

    private fun sendFile() {
        try {
            val jsch = JSch()
            val session = jsch.getSession(user, hostname, port)
            session.setPassword(password)
            val prop = Properties()
            prop["StrictHostKeyChecking"] = "no"
            session.setConfig(prop)
            session.connect(Int.MAX_VALUE)
            val sftpChannel = session.openChannel("sftp") as ChannelSftp
            sftpChannel.connect()
            sftpChannel.put(filePath, ActionBuildCommandUtility.RESOURCES_FOLDER_PATH)
        } catch (e: JSchException) {
            Log.w(TAG_DEBUG, "ERROR:" + e.message)
        } catch (e: SftpException) {
            Log.w(TAG_DEBUG, "ERROR:" + e.message)
        }
    }

    fun addPath(path: String?) {
        filePath = path
    }

    companion object {
        private const val TAG_DEBUG = "LGConnectionSendFile"
        private var instance: LGConnectionSendFile? = null
        @JvmStatic
        fun getInstance(): LGConnectionSendFile? {
            if (instance == null) {
                instance = LGConnectionSendFile()
            }
            return instance
        }
    }
}