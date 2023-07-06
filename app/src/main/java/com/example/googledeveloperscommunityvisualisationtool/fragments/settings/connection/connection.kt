package com.example.googledeveloperscommunityvisualisationtool.fragments.settings.connection

import android.app.Dialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.googledeveloperscommunityvisualisationtool.connection.LGCommand
import com.example.googledeveloperscommunityvisualisationtool.connection.LGConnectionManager
import com.example.googledeveloperscommunityvisualisationtool.connection.LGConnectionSendFile
import com.example.googledeveloperscommunityvisualisationtool.dialog.CustomDialogUtility
import com.example.googledeveloperscommunityvisualisationtool.R
import com.example.googledeveloperscommunityvisualisationtool.create.utility.model.ActionController
import com.example.googledeveloperscommunityvisualisationtool.databinding.FragmentConnectionBinding
import com.example.googledeveloperscommunityvisualisationtool.utility.ConstantPrefs
import java.util.regex.Pattern


class connection : Fragment() {
    private lateinit var binding:FragmentConnectionBinding
    private lateinit var sharedPref:SharedPreferences
    private lateinit var connectButton:Button
    private lateinit var lgipAddress:EditText
    private lateinit var passwordEditText: EditText
    private lateinit var lgnameEditText: EditText
    private lateinit var connectingTextView:TextView
    var handler=Handler()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=FragmentConnectionBinding.inflate(layoutInflater, container, false)
        val view=binding.root

        sharedPref= activity?.getSharedPreferences("didShowPrompt", Context.MODE_PRIVATE)!!
        val prefEdit=sharedPref?.edit()

        lgipAddress=binding.IpAddressEditText
        passwordEditText=binding.LgPassword
        lgnameEditText=binding.LgNameEditText
        connectButton=binding.ConnectLGButton
        connectingTextView=binding.ConnectingTextView

        connectButton.setOnClickListener { connectionTest() }




        return view
    }

    private fun connectionTest() {
        val hostPort = lgipAddress!!.text.toString()
        val usernameText = lgnameEditText!!.text.toString()
        val passwordText = passwordEditText!!.text.toString()
        saveConnectionInfo(hostPort, usernameText, passwordText)
        if (!isValidHostNPort(hostPort)) {
            CustomDialogUtility.showDialog(
                requireActivity(),
                resources.getString(R.string.activity_connection_host_port_error)
            )
            return
        }
        val dialog = CustomDialogUtility.getDialog(requireActivity(), resources.getString(R.string.connecting))
        dialog.setCanceledOnTouchOutside(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        changeButtonTextConnecting()
        createLgCommand(hostPort, usernameText, passwordText, dialog)
    }
    private fun createLgCommand(
        hostPort: String,
        usernameText: String,
        passwordText: String,
        dialog: Dialog
    ) {
        val command = "echo 'connection';"
        val lgCommand =
            LGCommand(command, LGCommand.CRITICAL_MESSAGE,object:LGCommand.Listener{
                override fun onResponse(response: String?) {
                    dialog.dismiss()                 }
            })
        createConnection(usernameText, passwordText, hostPort, lgCommand)
        sendMessageError(lgCommand, dialog)
    }
    private fun sendMessageError(lgCommand: LGCommand, dialog: Dialog) {
        handler.postDelayed({
            if (dialog.isShowing) {
                LGConnectionManager.getInstance()?.removeCommandFromLG(lgCommand)
                dialog.dismiss()
                CustomDialogUtility.showDialog(
                    requireActivity(),
                    resources.getString(R.string.activity_connection_error_connect)
                )
                val editor =
                    activity?.getSharedPreferences(ConstantPrefs.SHARED_PREFS.name, MODE_PRIVATE)?.edit()
                editor?.putBoolean(ConstantPrefs.IS_CONNECTED.name, false)
                editor?.apply()
                connectingTextView!!.visibility = View.INVISIBLE
                connectButton!!.visibility = View.VISIBLE
                connectButton!!.text = resources.getString(R.string.button_try_again)
            } else {
                CustomDialogUtility.showDialog(
                    requireActivity(),
                    resources.getString(R.string.activity_connection_success)
                )
                val editor =
                    activity?.getSharedPreferences(ConstantPrefs.SHARED_PREFS.name, MODE_PRIVATE)?.edit()
                editor?.putBoolean(ConstantPrefs.IS_CONNECTED.name, true)
                editor?.apply()
                changeToNewView()
                ActionController.instance?.sendBalloonWithLogos(requireActivity())
            }
        }, 2000)
    }

    private fun saveConnectionInfo(hostPort: String, usernameText: String, passwordText: String) {
        val editor = activity?.getSharedPreferences(ConstantPrefs.SHARED_PREFS.name, MODE_PRIVATE)?.edit()
        editor?.putString(ConstantPrefs.URI_TEXT.name, hostPort)
        editor?.putString(ConstantPrefs.USER_NAME.name, usernameText)
        editor?.putString(ConstantPrefs.USER_PASSWORD.name, passwordText)
        editor?.putBoolean(ConstantPrefs.TRY_TO_RECONNECT.name, true)
        editor?.apply()
    }


        private fun changeToNewView() {
            connectButton!!.visibility = View.INVISIBLE
            connectingTextView!!.visibility = View.INVISIBLE
            lgipAddress!!.visibility = View.INVISIBLE
            lgnameEditText!!.visibility = View.INVISIBLE
            passwordEditText!!.visibility = View.INVISIBLE
//            textUsername!!.visibility = View.INVISIBLE
//            textPassword!!.visibility = View.INVISIBLE
//            textInsertUrl!!.visibility = View.INVISIBLE
//            logo!!.visibility = View.VISIBLE
//            buttTryAgain!!.visibility = View.VISIBLE

    }

    private fun createConnection(
        username: String,
        password: String,
        hostPort: String,
        lgCommand: LGCommand
    ) {
        val hostNPort = hostPort.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val hostname = hostNPort[0]
        val port = hostNPort[1].toInt()
        val lgConnectionManager = LGConnectionManager.getInstance()
        lgConnectionManager!!.setData(username, password, hostname, port)
        lgConnectionManager.startConnection()
        lgConnectionManager.addCommandToLG(lgCommand)
        LGConnectionSendFile.getInstance()?.setData(username, password, hostname, port)
    }
    private fun changeButtonTextConnecting() {
        connectButton!!.visibility = View.INVISIBLE
        connectingTextView!!.visibility = View.VISIBLE
        connectingTextView!!.text = resources.getString(R.string.connecting)
    }

    private fun isValidHostNPort(hostPort: String): Boolean {
        return HOST_PORT.matcher(hostPort).matches()
    }
    companion object {
        //private static final String TAG_DEBUG = "MainActivity";
        private val HOST_PORT =
            Pattern.compile("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]):[0-9]+$")
    }

}