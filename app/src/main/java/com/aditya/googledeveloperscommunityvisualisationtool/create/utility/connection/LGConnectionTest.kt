package com.aditya.googledeveloperscommunityvisualisationtool.create.utility.connection

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Handler
import androidx.fragment.app.FragmentActivity
import com.aditya.googledeveloperscommunityvisualisationtool.MainActivity
import com.aditya.googledeveloperscommunityvisualisationtool.connection.LGCommand
import com.aditya.googledeveloperscommunityvisualisationtool.connection.LGConnectionManager.Companion.getInstance
import com.aditya.googledeveloperscommunityvisualisationtool.dialog.CustomDialogUtility
import com.aditya.googledeveloperscommunityvisualisationtool.R
import com.aditya.googledeveloperscommunityvisualisationtool.utility.ConstantPrefs
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
            R.drawable.warning_popup,
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
                    R.drawable.failed_poput,
                    "OOPS!, Failed to Connect",
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