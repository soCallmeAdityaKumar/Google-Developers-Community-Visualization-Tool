package com.aditya.googledeveloperscommunityvisualisationtool.fragments.settings.connection

import android.app.Dialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.Display.Mode
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieAnimationView
import com.aditya.googledeveloperscommunityvisualisationtool.MainActivity
import com.aditya.googledeveloperscommunityvisualisationtool.connection.LGCommand
import com.aditya.googledeveloperscommunityvisualisationtool.connection.LGConnectionManager
import com.aditya.googledeveloperscommunityvisualisationtool.connection.LGConnectionSendFile
import com.aditya.googledeveloperscommunityvisualisationtool.dialog.CustomDialogUtility
import com.aditya.googledeveloperscommunityvisualisationtool.TextToSpeech.TextToSpeechClass
import com.aditya.googledeveloperscommunityvisualisationtool.create.utility.model.ActionBuildCommandUtility
import com.aditya.googledeveloperscommunityvisualisationtool.create.utility.model.ActionController
import com.aditya.googledeveloperscommunityvisualisationtool.utility.ConstantPrefs
import com.aditya.googledeveloperscommunityvisualisationtool.R
import com.aditya.googledeveloperscommunityvisualisationtool.databinding.FragmentConnectionBinding
import java.util.regex.Pattern


class connection : Fragment() {
    private lateinit var binding: FragmentConnectionBinding
    private lateinit var sharedPref:SharedPreferences
    private lateinit var connectButton:Button
    private lateinit var lgipAddress:EditText
    private lateinit var port:EditText
    private lateinit var passwordEditText: EditText
    private lateinit var lgnameEditText: EditText
    private lateinit var connectingTextView:TextView
    private lateinit var buttTryAgain:Button
    lateinit var  textToSpeech:TextToSpeechClass
    private lateinit var passwordEye:ImageView
    private lateinit var passwordSharePref:SharedPreferences
    private lateinit var passwordEditor:SharedPreferences.Editor
    var handler=Handler()
    var screenAmount=5
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=FragmentConnectionBinding.inflate(layoutInflater, container, false)
        val view=binding.root


        lgipAddress=binding.IpAddressEditText
        passwordEditText=binding.LgPassword
        lgnameEditText=binding.LgNameEditText
        port=binding.PortEditText
        connectButton=binding.ConnectLGButton
        connectingTextView=binding.ConnectingTextView
        buttTryAgain=binding.buttTryAgain

        passwordEye=binding.PasswordEye

        passwordSharePref=activity?.getSharedPreferences("PasswordEye",Context.MODE_PRIVATE)!!
        passwordEditor=passwordSharePref.edit()
        passwordEditor.apply {
            putBoolean("EYEClose",true)
            apply()
        }
        if(passwordSharePref.getBoolean("EYEClose",true)){
            passwordEye.setImageDrawable(resources.getDrawable(R.drawable.eye_close))
            passwordEditText.transformationMethod=PasswordTransformationMethod.getInstance()
        }
        passwordEye.setOnClickListener {
            if(passwordSharePref.getBoolean("EYEClose",true)){
                passwordEye.setImageDrawable(resources.getDrawable(R.drawable.eye_close))
                passwordEditText.transformationMethod=PasswordTransformationMethod.getInstance()
                passwordEditor.apply{
                    putBoolean("EYEClose",false)
                    apply()
                }
            }else{
                passwordEye.setImageDrawable(resources.getDrawable(R.drawable.eye_open))
                passwordEditText.transformationMethod=HideReturnsTransformationMethod.getInstance()
                passwordEditor.apply{
                    putBoolean("EYEClose",true)
                    apply()
                }
            }
        }

        val lgsharedPref=activity?.getSharedPreferences(ConstantPrefs.SHARED_PREFS.name,MODE_PRIVATE)!!
        val isConnected=lgsharedPref.getBoolean("IS_CONNECTED",false)
        if(isConnected){
            passwordEditText.setText(lgsharedPref.getString("USER_PASSWORD",null))
            val hostNPort = lgsharedPref.getString("URI_TEXT",null)!!.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            lgipAddress.setText(hostNPort[0])
            port.setText(hostNPort[1])
            lgnameEditText.setText(lgsharedPref.getString("USER_NAME",null))
        }
        else{
            if(lgsharedPref.contains("URI_TEXT")){
                loadSharedData()
            }

        }

        connectButton.setOnClickListener { connectionTest()}
        buttTryAgain?.setOnClickListener(View.OnClickListener { view: View? ->
            changeToMainView()
            val editor = activity?.getSharedPreferences(ConstantPrefs.SHARED_PREFS.name, MODE_PRIVATE)!!
                .edit()
            editor.putBoolean(ConstantPrefs.IS_CONNECTED.name, false)
            editor.apply()
        })


        return view
    }



    override fun onResume() {
            super.onResume()
            val customAppBar = (activity as MainActivity).binding.appBarMain
            val menuButton = customAppBar.menuButton

            val navController = findNavController()
            val isRootFragment = navController.graph.startDestinationId == navController.currentDestination?.id

        if (isRootFragment) {
            menuButton.setBackgroundResource(R.drawable.baseline_menu_24)
//            menuButton?.visibility = View.VISIBLE
//            backButton?.visibility = View.GONE
        } else {
            menuButton.setBackgroundResource(R.drawable.backarrow)
//            menuButton?.visibility = View.GONE
//            backButton?.visibility = View.VISIBLE
            menuButton?.setOnClickListener {
                (activity as MainActivity).onBackPressed()
            }
        }


    }

     private fun connectionTest() {
         if(port.text.isEmpty()||lgipAddress.text.isEmpty()||lgnameEditText.text.isEmpty()||passwordEditText.text.isEmpty()){
             if(port.text.isEmpty()&&lgipAddress.text.isEmpty()&&lgnameEditText.text.isEmpty()&&passwordEditText.text.isEmpty()){
               CustomDialogUtility.showDialog(requireActivity(),R.drawable.warning_popup,"OOPS!, Something went wrong","You haven't Enter Any Details!!")
             }
             else if(port.text.isEmpty()){
                 CustomDialogUtility.showDialog(requireActivity(),R.drawable.warning_popup,"OOPS!, Something went wrong","Please enter the Port Number!!")
             }else if(lgipAddress.text.isEmpty()){
                CustomDialogUtility.showDialog(requireActivity(),R.drawable.warning_popup,"OOPS!, Something went wrong","Please enter the Ip Address!!")

             }else if(lgnameEditText.text.isEmpty()){
                 CustomDialogUtility.showDialog(requireActivity(),R.drawable.warning_popup,"OOPS!, Something went wrong","Please enter LG Name!!")
             }else if(passwordEditText.text.isEmpty()){
               CustomDialogUtility.showDialog(requireActivity(),R.drawable.warning_popup,"OOPS!, Something went wrong","Please enter the Password!!")
             }
         }else{
             val port = port!!.text.toString()
             val host=lgipAddress!!.text.toString()
             val usernameText = lgnameEditText!!.text.toString()
             val passwordText = passwordEditText!!.text.toString()
             saveConnectionInfo(host,port, usernameText, passwordText)
             if (!isValidHostNPort("$host:$port")) {
                 CustomDialogUtility.showDialog(
                     requireActivity(),
                     R.drawable.failed_poput,
                     "OOPS!, Something went wrong",
                     resources.getString(R.string.activity_connection_host_port_error)
                 )
                 val editor = activity?.getSharedPreferences(ConstantPrefs.SHARED_PREFS.name, MODE_PRIVATE)!!
                     .edit()
                 editor.putBoolean(ConstantPrefs.IS_CONNECTED.name, false)
                 editor.apply()
                 loadConnectionStatus()
                 return
             }
             val dialog = CustomDialogUtility.getDialog(requireActivity(),R.drawable.warning_popup, resources.getString(R.string.connecting))
             dialog.setCanceledOnTouchOutside(false)
             dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
             dialog.show()
             changeButtonTextConnecting()
             createLgCommand("$host:$port", usernameText, passwordText, dialog)
         }

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
                    R.drawable.failed_poput,
                    "OOPS!, Failed to Connect",
                    resources.getString(R.string.activity_connection_error_connect)
                )
                val editor =
                    activity?.getSharedPreferences(ConstantPrefs.SHARED_PREFS.name, MODE_PRIVATE)?.edit()
                editor?.putBoolean(ConstantPrefs.IS_CONNECTED.name, false)
                editor?.apply()
                loadConnectionStatus()
                connectingTextView!!.visibility = View.INVISIBLE
                connectButton!!.visibility = View.VISIBLE
                connectButton!!.text = resources.getString(R.string.button_try_again)
            } else {
                CustomDialogUtility.showDialog(
                    requireActivity(),
                    R.drawable.success_popup,
                    "Whoa! Connected to LG",
                    resources.getString(R.string.activity_connection_success),
                )
                val editor =
                    activity?.getSharedPreferences(ConstantPrefs.SHARED_PREFS.name, MODE_PRIVATE)?.edit()
                editor?.putBoolean(ConstantPrefs.IS_CONNECTED.name, true)
                editor?.apply()
                loadConnectionStatus()
                changeToNewView()
                ActionController.instance?.sendBalloonWithLogos(requireActivity())
            }
        }, 2000)
    }

    private fun saveConnectionInfo(host:String,Port: String, usernameText: String, passwordText: String) {
        val editor = activity?.getSharedPreferences(ConstantPrefs.SHARED_PREFS.name, MODE_PRIVATE)?.edit()
        editor?.putString(ConstantPrefs.URI_TEXT.name, "$host:$Port")
        editor?.putString(ConstantPrefs.USER_NAME.name, usernameText)
        editor?.putString(ConstantPrefs.USER_PASSWORD.name, passwordText)
        editor?.putBoolean(ConstantPrefs.TRY_TO_RECONNECT.name, false)
        editor?.apply()
    }


        private fun changeToNewView() {
            connectButton!!.visibility = View.VISIBLE
            connectingTextView!!.visibility = View.INVISIBLE
            lgipAddress!!.visibility = View.VISIBLE
            lgnameEditText!!.visibility = View.VISIBLE
            passwordEditText!!.visibility = View.VISIBLE


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

    private fun changeToMainView() {
        connectButton!!.visibility = View.VISIBLE
        connectingTextView!!.visibility = View.VISIBLE
        lgipAddress!!.visibility = View.VISIBLE
        lgnameEditText!!.visibility = View.VISIBLE
        passwordEditText!!.visibility = View.VISIBLE
        buttTryAgain!!.visibility = View.INVISIBLE
        loadSharedData()
    }

    private fun loadSharedData() {
        val sharedPreferences = activity?.getSharedPreferences(ConstantPrefs.SHARED_PREFS.name, MODE_PRIVATE)
        val isConnected = sharedPreferences?.getBoolean(ConstantPrefs.IS_CONNECTED.name, false)
        if (isConnected!!) {
            changeToNewView()
        } else {
            val hostNport = sharedPreferences.getString(ConstantPrefs.URI_TEXT.name, "")!!.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val iptext=hostNport[0]
            val porttext=hostNport[1]
            val usernameText = sharedPreferences.getString(ConstantPrefs.USER_NAME.name, "")
            val passwordText = sharedPreferences.getString(ConstantPrefs.USER_PASSWORD.name, "")
            val isTryToReconnect =
                sharedPreferences.getBoolean(ConstantPrefs.TRY_TO_RECONNECT.name, false)
            if (iptext != "") lgipAddress!!.setText(iptext)
            if(porttext!="") port!!.setText(porttext)
            if (usernameText != "") lgnameEditText!!.setText(usernameText)
            if (passwordText != "") passwordEditText!!.setText(passwordText)
            if (isTryToReconnect) connectButton!!.text =
                resources.getString(R.string.button_try_again)
        }
    }

    private fun loadConnectionStatus() {
        val sharedPreferences = activity?.getSharedPreferences(ConstantPrefs.SHARED_PREFS.name, MODE_PRIVATE)

        val isConnected = sharedPreferences?.getBoolean(ConstantPrefs.IS_CONNECTED.name, false)
        val act=activity as MainActivity
        if (isConnected!!) {
            act.binding.appBarMain.LGConnected.visibility=View.VISIBLE
            act.binding.appBarMain.LGNotConnected.visibility=View.INVISIBLE
        } else {
            act.binding.appBarMain.LGConnected.visibility=View.INVISIBLE
            act.binding.appBarMain.LGNotConnected.visibility=View.VISIBLE
        }
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