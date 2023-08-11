package com.example.googledeveloperscommunityvisualisationtool

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.example.googledeveloperscommunityvisualisationtool.databinding.ActivityMainBinding
import com.example.googledeveloperscommunityvisualisationtool.utility.ConstantPrefs

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
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController=findNavController(R.id.fragmentContainerView)
        return navController.navigateUp(appBarConfiguration)|| super.onSupportNavigateUp()
    }
    private fun loadConnectionStatus() {
        val sharedPreferences = getSharedPreferences(ConstantPrefs.SHARED_PREFS.name, MODE_PRIVATE)

        val isConnected = sharedPreferences?.getBoolean(ConstantPrefs.IS_CONNECTED.name, false)
        if (isConnected!!) {
            binding.appBarMain.connectionStatus.text="LG Connected"
            binding.appBarMain.connectionStatus.setTextColor(resources.getColor(R.color.Connected))
        } else {
            binding.appBarMain.connectionStatus.text="LG Not Connected"
            binding.appBarMain.connectionStatus.setTextColor(resources.getColor(R.color.NotConnected))

        }
    }





}