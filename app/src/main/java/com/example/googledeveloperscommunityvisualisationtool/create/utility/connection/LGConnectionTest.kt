package com.example.googledeveloperscommunityvisualisationtool.create.utility.connection

import android.content.Context
import android.os.Handler
import androidx.fragment.app.FragmentActivity
import com.example.googledeveloperscommunityvisualisationtool.connection.LGCommand
import com.example.googledeveloperscommunityvisualisationtool.connection.LGConnectionManager.Companion.getInstance
import com.example.googledeveloperscommunityvisualisationtool.dialog.CustomDialogUtility
import com.example.googledeveloperscommunityvisualisationtool.R
import com.example.googledeveloperscommunityvisualisationtool.utility.ConstantPrefs
import java.util.concurrent.atomic.AtomicBoolean

object LGConnectionTest {
    private val handler = Handler()

    /**
     * Test the connection with the previous data connection
     * @param atomicBoolean the boolean to know if the connection is succesful or not
     */
    @JvmStatic
    fun testPriorConnection(fragment:FragmentActivity, atomicBoolean: AtomicBoolean) {
        val dialog = CustomDialogUtility.getDialog(
            fragment,
            fragment.resources.getString(R.string.test_connection)
        )
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        val lgCommand = LGCommand(
            "echo 'connection';",
            LGCommand.CRITICAL_MESSAGE,
            object :LGCommand.Listener {
                override fun onResponse(response: String?) {
                    dialog.dismiss()
                }
            })
        sendCommand(lgCommand)
        handler.postDelayed({
            val editor =
                fragment.getSharedPreferences(ConstantPrefs.SHARED_PREFS.name, Context.MODE_PRIVATE)
                    .edit()
            if (dialog.isShowing) {
                getInstance()!!.removeCommandFromLG(lgCommand)
                dialog.dismiss()
                editor.putBoolean(ConstantPrefs.IS_CONNECTED.name, false)
                CustomDialogUtility.showDialog(
                    fragment,
                    fragment.resources.getString(R.string.activity_connection_error)
                )
                atomicBoolean.set(false)
            } else {
                editor.putBoolean(ConstantPrefs.IS_CONNECTED.name, true)
                atomicBoolean.set(true)
            }
            editor.apply()
        }, 1000)
    }

    /**
     * Send a command to the liquid galaxy
     * @param lgCommand lg command to be send
     */
    private fun sendCommand(lgCommand: LGCommand) {
        val lgConnectionManager = getInstance()
        lgConnectionManager!!.startConnection()
        lgConnectionManager.addCommandToLG(lgCommand)
    }
}