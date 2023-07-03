package com.example.googledeveloperscommunityvisualisationtool.connection

import android.util.Log
import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.JSch
import com.jcraft.jsch.JSchException
import com.jcraft.jsch.Session
import java.io.IOException
import java.util.Objects
import java.util.Properties
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingDeque

class LGConnectionManager private constructor() : Runnable {
    var user = "lg1"
        private set
    var password = "lg"
        private set
    var hostname = "192.168.153.3"
        private set
    var port = 22
        private set
    private var session: Session? = null
    private val queue: BlockingQueue<LGCommand>
    private var itemsToDequeue: Int
    private var lgCommandToReSend: LGCommand?
    private var activity: ILGConnection? = null

    /**
     * Enforce private constructor and add the default values
     */
    init {
        queue = LinkedBlockingDeque()
        itemsToDequeue = 0
        lgCommandToReSend = null
    }

    fun setData(user: String, password: String, hostname: String, port: Int) {
        this.user = user
        this.password = password
        this.hostname = hostname
        this.port = port
        session = null
        tick()
        addCommandToLG(LGCommand("echo 'connection';", LGCommand.CRITICAL_MESSAGE, null))
    }

    fun startConnection() {
        Thread(instance).start()
        statusUpdater = StatusUpdater(instance!!)
        Thread(statusUpdater).start()
    }

    override fun run() {
        try {
            do {
                var lgCommand = lgCommandToReSend
                if (lgCommand == null) {
                    lgCommand = queue.take()
                    if (itemsToDequeue > 0) {
                        itemsToDequeue--
                        if (lgCommand.priorityType == LGCommand.CRITICAL_MESSAGE) {
                            lgCommandToReSend = lgCommand
                        }
                        continue
                    }
                }
                val timeBefore = System.currentTimeMillis()
                if (!sendLGCommand(lgCommand)) {
                    //Command not sent
                    itemsToDequeue = queue.size
                } else if (System.currentTimeMillis() - timeBefore >= 2000) {
                    //Command sent but took more than logos seconds
                    lgCommandToReSend = null
                    itemsToDequeue = queue.size
                } else {
                    //Command sent in less than logos seconds
                    lgCommandToReSend = null
                }
            } while (true)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    /**
     * The method that send a lgcommand
     *
     * @param lgCommand LgCommand that is going to be send
     * @return boolean of if the lgCommand is send or no
     */
    private fun sendLGCommand(lgCommand: LGCommand?): Boolean {
        lgCommandToReSend = lgCommand
        Log.d("ConnectionManager", "sending a lgcommand: " + lgCommand!!.command)
        val session = getSession()
        if (session == null || !session.isConnected) {
            Log.d("ConnectionManager", "session not connected: " + lgCommand.command)
            return false
        }
        return try {
            val channelSsh = session.openChannel("exec") as ChannelExec
            val outputBuffer =
                if (lgCommand.priorityType == LGCommand.CRITICAL_MESSAGE) StringBuilder() else null
            val commandOutput = channelSsh.inputStream
            channelSsh.setCommand(lgCommand.command)
            channelSsh.connect()
            if (lgCommand.priorityType == LGCommand.CRITICAL_MESSAGE) {
                var readByte = commandOutput.read()
                while (readByte != -0x1) {
                    Objects.requireNonNull(outputBuffer)?.append(readByte.toChar())
                    readByte = commandOutput.read()
                }
            }
            channelSsh.disconnect()
            var response = ""
            if (lgCommand.priorityType == LGCommand.CRITICAL_MESSAGE) {
                response = Objects.requireNonNull(outputBuffer).toString()
                Log.w(TAG_DEBUG, "response: $response")
            }
            lgCommand.doAction(response)
            true
        } catch (jSchException: JSchException) {
            Log.w(TAG_DEBUG, "response: " + jSchException.message)
            false
        } catch (iOException: IOException) {
            Log.w(TAG_DEBUG, "couldn't get InputStream or read from it: " + iOException.message)
            false
        }
    }

    /**
     * Create a session if the old session is null
     *
     * @return Session
     */
    private fun getSession(): Session? {
        val oldSession = session
        return if (oldSession == null || !oldSession.isConnected) {
            try {
                val jsch = JSch()
                val session = jsch.getSession(user, hostname, port)
                session.setPassword(password)
                val prop = Properties()
                prop["StrictHostKeyChecking"] = "no"
                session.setConfig(prop)
                session.connect(Int.MAX_VALUE)
                this.session = session
                session
            } catch (e: JSchException) {
                Log.w(TAG_DEBUG, "response create new session: " + e.message)
                null
            }
        } else try {
            oldSession.sendKeepAliveMsg()
            oldSession
        } catch (e: Exception) {
            Log.w(TAG_DEBUG, "response old session: " + e.message)
            null
        }
    }

    fun tick() {
        val activityCopy = activity
        if (activityCopy != null) {
            val oldSession = session
            if (oldSession == null || !oldSession.isConnected) {
                activityCopy.setStatus(NOT_CONNECTED)
            } else if (lgCommandToReSend == null && queue.size == 0) {
                activityCopy.setStatus(CONNECTED)
            } else {
                activityCopy.setStatus(QUEUE_BUSY)
            }
        }
    }

    fun removeActivity(activity: ILGConnection) {
        if (this.activity === activity) this.activity = null
    }

    fun setActivity(activity: ILGConnection?) {
        this.activity = activity
    }

    fun addCommandToLG(lgCommand: LGCommand) {
        queue.offer(lgCommand)
    }

    fun removeCommandFromLG(lgCommand: LGCommand): Boolean {
        if (lgCommand == lgCommandToReSend) {
            lgCommandToReSend = null
        }
        return queue.remove(lgCommand)
    }

    fun containsCommandFromLG(lgCommand: LGCommand): Boolean {
        return lgCommand == lgCommandToReSend || queue.contains(lgCommand)
    }

    companion object {
        private const val TAG_DEBUG = "LGConnectionManager"
        const val CONNECTED: Short = 1
        const val NOT_CONNECTED: Short = 2
        const val QUEUE_BUSY: Short = 3
        private var instance: LGConnectionManager? = null
        private var statusUpdater: StatusUpdater? = null
        @JvmStatic
        fun getInstance(): LGConnectionManager? {
            if (instance == null) {
                instance = LGConnectionManager()
            }
            return instance
        }
    }
}