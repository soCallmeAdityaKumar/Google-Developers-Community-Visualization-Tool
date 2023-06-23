package com.example.googledeveloperscommunityvisualisationtool.Connection

import android.os.Handler
import android.os.Looper

class LGCommand(val command: String, val priorityType: Short, private val listener: Listener?) {
    /** Callback interface for delivering parsed responses.  */
    interface Listener {
        /** Called when a response is received.  */
        fun onResponse(response: String?)
    }

    fun doAction(response: String?) {
        if (listener != null) Handler(Looper.getMainLooper()).post { listener.onResponse(response) }
    }

    companion object {
        const val NON_CRITICAL_MESSAGE: Short = 0
        const val CRITICAL_MESSAGE: Short = 1
    }
}