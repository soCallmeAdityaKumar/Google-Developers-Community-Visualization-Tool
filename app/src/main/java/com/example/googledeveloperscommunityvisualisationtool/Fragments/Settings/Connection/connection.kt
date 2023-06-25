package com.example.googledeveloperscommunityvisualisationtool.Fragments.Settings.Connection

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.GravityCompat
import com.example.googledeveloperscommunityvisualisationtool.Connection.LGCommand
import com.example.googledeveloperscommunityvisualisationtool.Connection.LGConnectionManager
import com.example.googledeveloperscommunityvisualisationtool.Connection.LGConnectionSendFile
import com.example.googledeveloperscommunityvisualisationtool.Dialog.CustomDialogUtility
import com.example.googledeveloperscommunityvisualisationtool.Fragments.Settings.Settings
import com.example.googledeveloperscommunityvisualisationtool.R
import com.example.googledeveloperscommunityvisualisationtool.create.utility.model.ActionController
import com.example.googledeveloperscommunityvisualisationtool.databinding.FragmentConnectionBinding
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetView
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

        showIpAddresstap()
        lgipAddress=binding.IpAddressEditText
        passwordEditText=binding.LgPassword
        lgnameEditText=binding.IpAddressEditText
        connectButton=binding.ConnectLGButton
        connectingTextView=binding.ConnectingTextView

        connectButton.setOnClickListener { connectionTest() }



        return view
    }

    private fun connectionTest() {
        val hostPort = lgipAddress!!.text.toString()
        val usernameText = lgnameEditText!!.text.toString()
        val passwordText = passwordEditText!!.text.toString()
//        saveConnectionInfo(hostPort, usernameText, passwordText)
        if (!isValidHostNPort(hostPort)) {
            CustomDialogUtility.showDialog(
                requireActivity(),
                resources.getString(R.string.activity_connection_host_port_error)
            )
            return
        }
        val dialog = CustomDialogUtility.getDialog(requireActivity(), resources.getString(R.string.connecting))
        dialog.setCanceledOnTouchOutside(false)
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
//                val editor =
//                    getSharedPreferences(ConstantPrefs.SHARED_PREFS.name, MODE_PRIVATE).edit()
//                editor.putBoolean(ConstantPrefs.IS_CONNECTED.name, false)
//                editor.apply()
                connectingTextView!!.visibility = View.INVISIBLE
                connectButton!!.visibility = View.VISIBLE
                connectButton!!.text = resources.getString(R.string.button_try_again)
            } else {
                CustomDialogUtility.showDialog(
                    requireActivity(),
                    resources.getString(R.string.activity_connection_success)
                )
//                val editor =
//                    getSharedPreferences(ConstantPrefs.SHARED_PREFS.name, MODE_PRIVATE).edit()
//                editor.putBoolean(ConstantPrefs.IS_CONNECTED.name, true)
//                editor.apply()
                changeToNewView()
                ActionController.instance?.sendBalloonWithLogos(requireActivity())
            }
        }, 2000)
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


    private fun showIpAddresstap() {
        if(!sharedPref.getBoolean("didShowPrompt",false)){
            TapTargetView.showFor(this.requireActivity(),
                TapTarget.forView(
                    binding.IpAddressEditText,
                    "Enter the Main Slave Ip Address ",
                    "")
                    .outerCircleColor(androidx.appcompat.R.color.material_deep_teal_200)
                    .outerCircleAlpha(0.96f)
                    .targetCircleColor(R.color.white)
                    .titleTextSize(20)
                    .titleTextColor(R.color.white)
                    .descriptionTextSize(10)
                    .descriptionTextColor(R.color.black)
                    .textColor(R.color.black)
                    .textTypeface(Typeface.SANS_SERIF)
                    .dimColor(R.color.black)
                    .drawShadow(true)
                    .cancelable(false)
                    .tintTarget(true)
                    .transparentTarget(true)
                    .targetRadius(60),
                object : TapTargetView.Listener() {
                    override fun onTargetClick(view: TapTargetView?) {
                        super.onTargetClick(view)
                        showLgNametap()
                    }
                })
        }

    }



    private fun showLgNametap() {
        if(!sharedPref.getBoolean("didShowPrompt",false)){
            TapTargetView.showFor(this.requireActivity(),
                TapTarget.forView(
                    binding.LgNameEditText,
                    "Enter the Lg Name  ",
                    "")
                    .outerCircleColor(androidx.appcompat.R.color.material_deep_teal_200)
                    .outerCircleAlpha(0.96f)
                    .targetCircleColor(R.color.white)
                    .titleTextSize(20)
                    .titleTextColor(R.color.white)
                    .descriptionTextSize(10)
                    .descriptionTextColor(R.color.black)
                    .textColor(R.color.black)
                    .textTypeface(Typeface.SANS_SERIF)
                    .dimColor(R.color.black)
                    .drawShadow(true)
                    .cancelable(false)
                    .tintTarget(true)
                    .transparentTarget(true)
                    .targetRadius(60),
                object : TapTargetView.Listener() {
                    override fun onTargetClick(view: TapTargetView?) {
                        super.onTargetClick(view)
                        showPasswordtap()
                    }
                })
        }

    }

    private fun showPasswordtap() {
        if(!sharedPref.getBoolean("didShowPrompt",false)){
            TapTargetView.showFor(this.requireActivity(),
                TapTarget.forView(
                    binding.LgPassword,
                    "Enter the Main Slave Password ",
                    "")
                    .outerCircleColor(androidx.appcompat.R.color.material_deep_teal_200)
                    .outerCircleAlpha(0.96f)
                    .targetCircleColor(R.color.white)
                    .titleTextSize(20)
                    .titleTextColor(R.color.white)
                    .descriptionTextSize(10)
                    .descriptionTextColor(R.color.black)
                    .textColor(R.color.black)
                    .textTypeface(Typeface.SANS_SERIF)
                    .dimColor(R.color.black)
                    .drawShadow(true)
                    .cancelable(false)
                    .tintTarget(true)
                    .transparentTarget(true)
                    .targetRadius(60),
                object : TapTargetView.Listener() {
                    override fun onTargetClick(view: TapTargetView?) {
                        super.onTargetClick(view)
                        val prefEditor = sharedPref.edit()
                        prefEditor.putBoolean("didShowPrompt", true)
                        prefEditor.apply()
                    }
                })
        }

    }

}