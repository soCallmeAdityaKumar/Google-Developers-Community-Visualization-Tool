package com.aditya.googledeveloperscommunityvisualisationtool

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.aditya.googledeveloperscommunityvisualisationtool.utility.ConstantPrefs
import com.aditya.googledeveloperscommunityvisualisationtool.R
import com.aditya.googledeveloperscommunityvisualisationtool.connection.LGCommand
import com.aditya.googledeveloperscommunityvisualisationtool.connection.LGConnectionManager
import com.aditya.googledeveloperscommunityvisualisationtool.connection.LGConnectionSendFile
import com.aditya.googledeveloperscommunityvisualisationtool.create.utility.model.ActionController
import com.aditya.googledeveloperscommunityvisualisationtool.databinding.ActivityMainBinding
import com.aditya.googledeveloperscommunityvisualisationtool.dialog.CustomDialogUtility
import com.aditya.googledeveloperscommunityvisualisationtool.fragments.settings.connection.connection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var sharedPref:SharedPreferences
    lateinit var prefEdit:SharedPreferences.Editor
    private lateinit var navController: NavController
    private lateinit var drawerLayout:DrawerLayout
    private lateinit var themeSharedPreferences: SharedPreferences
    lateinit var notifyImage:ImageView
    var storedgdgData=0
    var mainActivityPieMaking=0
    var handler=Handler()
    var delayMillis:Long=10000

    companion object {
        //private static final String TAG_DEBUG = "MainActivity";
        private val HOST_PORT =
            Pattern.compile("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]):[0-9]+$")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.fragmentContainerView)
        drawerLayout = binding.drawerlayout
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        binding.navView.setupWithNavController(navController)
        notifyImage=binding.appBarMain.notifyImage

        binding.appBarMain.menuButton.setOnClickListener {
            binding.drawerlayout.openDrawer(GravityCompat.START)
        }
        themeSharedPreferences=this.getSharedPreferences("Theme",Context.MODE_PRIVATE)
        val night=themeSharedPreferences.getBoolean("Night",false)
        if(night){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            binding.appBarMain.notifyImage.setImageDrawable(resources.getDrawable(R.drawable.notify_dark_logo))
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            binding.appBarMain.notifyImage.setImageDrawable(resources.getDrawable(R.drawable.notify_light_logo))

        }

        var handle=handler.postDelayed(object : Runnable {
            override fun run() {
                checkConnection()
                handler.postDelayed(this, delayMillis)
            }
        }, delayMillis)
//        loadConnectionStatus()

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            binding.appBarMain.titleactionbar.text = when (destination.id) {
                R.id.settings -> resources.getString(R.string.Settings)
                R.id.upcomingEvents -> resources.getString(R.string.UpcomingEvent)
                R.id.home -> resources.getString(R.string.Home)
                R.id.oldGdgList -> resources.getString(R.string.Old_Data)
                R.id.calender->resources.getString(R.string.Calendar)
                R.id.gdgChapterDetails -> resources.getString(R.string.GDG_ChapterDetails)
                R.id.notification->resources.getString(R.string.notification)
                R.id.connection->resources.getString(R.string.connection)
                R.id.oldEvent->resources.getString(R.string.old_event)
                R.id.lastWeekEventDetails->getString(R.string.event_details)
                R.id.upcomingEventDetails->getString(R.string.upcomingevent_details)
                else -> resources.getString(R.string.app_name)
            }
            binding.appBarMain.notifyImage.setImageDrawable(getDrawable(R.drawable.notify_light_logo))
            notifyImage.setOnClickListener {
                controller.navigate(R.id.notification)
            }

        }

//        val isRootFragment = navController.graph.startDestinationId == navController.currentDestination?.id
//        if(isRootFragment){
//            binding.appBarMain.menuButton.setBackgroundResource(R.drawable.baseline_menu_24)
//        }else{
//            binding.appBarMain.menuButton.setBackgroundResource(R.drawable.backarrow)
////            menuButton?.visibility = View.GONE
////            backButton?.visibility = View.VISIBLE
//            binding.appBarMain.menuButton?.setOnClickListener{
//                this.onBackPressed()
//            }
//        }
    }

    private fun checkConnection() {
        var connectionPref = getSharedPreferences(ConstantPrefs.SHARED_PREFS.name, Context.MODE_PRIVATE)
            if (connectionPref.getString(ConstantPrefs.URI_TEXT.name, "")!!.isNotEmpty()) {
                var timeout=0
                val hostNport =
                    connectionPref.getString(ConstantPrefs.URI_TEXT.name, "")!!.split(":".toRegex())
                        .dropLastWhile { it.isEmpty() }.toTypedArray()
                var iptext = ""
                var porttext = 0
                if (hostNport.isNotEmpty()) {
                    iptext = hostNport[0]
                    if(isValidHostNPort("${hostNport[0]}:${hostNport[1]}")){
                        iptext=hostNport[0]
                        porttext=hostNport[1].toInt()
                    }

                }
                val usernameText = connectionPref.getString(ConstantPrefs.USER_NAME.name, "")
                val passwordText = connectionPref.getString(ConstantPrefs.USER_PASSWORD.name, "")
//            val isTryToReconnect =
//                connectionPref.getBoolean(ConstantPrefs.TRY_TO_RECONNECT.name, false)
                if (iptext.isNotEmpty() && porttext.toString()
                        .isNotEmpty() && usernameText!!.isNotEmpty() && passwordText!!.isNotEmpty()
                ) {
                    Log.d("ConnectionMain LGConnectionManager", "not empty")
                    val command = "echo 'connection';"
                    val lgCommand =
                        LGCommand(command, LGCommand.CRITICAL_MESSAGE, object : LGCommand.Listener {
                            override fun onResponse(response: String?) {
                                timeout=1
                                delayMillis=10000
                                val editor =
                                    getSharedPreferences(ConstantPrefs.SHARED_PREFS.name, MODE_PRIVATE)?.edit()
                                editor?.putBoolean(ConstantPrefs.IS_CONNECTED.name, true)
                                editor?.apply()
                                Log.d("ConnectionMain LGConnectionManager","timeout->$timeout   response->$response  username->$usernameText  password->$passwordText ip->$iptext   port->$porttext"  )
                                loadConnectionStatus()
                            }
                        })
                    val lgConnectionManager = LGConnectionManager.getInstance()
                    lgConnectionManager!!.setData(usernameText, passwordText, iptext, porttext)
                    lgConnectionManager.startConnection()
                    lgConnectionManager.addCommandToLG(lgCommand)
                    LGConnectionSendFile.getInstance()
                        ?.setData(usernameText, passwordText, iptext, porttext)
                    CoroutineScope(Dispatchers.IO).launch {
                        delay(2000)
                        sendMessageError(lgCommand,timeout)
                    }

                }
            }
    }

        private fun sendMessageError(lgCommand: LGCommand,timeOut:Int) {
            handler.postDelayed({
                if (timeOut==0) {
                    LGConnectionManager.getInstance()?.removeCommandFromLG(lgCommand)
                    val editor =
                        getSharedPreferences(ConstantPrefs.SHARED_PREFS.name, MODE_PRIVATE)?.edit()
                    editor?.putBoolean(ConstantPrefs.IS_CONNECTED.name, false)
                    editor?.apply()
                    delayMillis=5000
                    Log.d("ConnectionMain LGConnectionManager","Not connected $delayMillis  $timeOut")
                    loadConnectionStatus()

                } else if(timeOut==1) {
                    delayMillis=10000
                    val editor =
                        getSharedPreferences(ConstantPrefs.SHARED_PREFS.name, MODE_PRIVATE)?.edit()
                    editor?.putBoolean(ConstantPrefs.IS_CONNECTED.name, true)
                    editor?.apply()
                    Log.d("ConnectionMain LGConnectionManager","connected  $delayMillis")
                    loadConnectionStatus()
                }
            },2000)

        }



    override fun onSupportNavigateUp(): Boolean {
        val navController=findNavController(R.id.fragmentContainerView)
        return navController.navigateUp(appBarConfiguration)|| super.onSupportNavigateUp()
    }
    private fun loadConnectionStatus() {
        val sharedPreferences = getSharedPreferences(ConstantPrefs.SHARED_PREFS.name, MODE_PRIVATE)

        val isConnected = sharedPreferences?.getBoolean(ConstantPrefs.IS_CONNECTED.name, false)
        if (isConnected!!) {
            binding.appBarMain.LGConnected.visibility=View.VISIBLE
            binding.appBarMain.LGNotConnected.visibility=View.INVISIBLE
        } else {
            binding.appBarMain.LGConnected.visibility=View.INVISIBLE
            binding.appBarMain.LGNotConnected.visibility=View.VISIBLE
        }
    }
    private fun isValidHostNPort(hostPort: String): Boolean {
        return MainActivity.HOST_PORT.matcher(hostPort).matches()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)

    }
}