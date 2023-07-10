package com.example.googledeveloperscommunityvisualisationtool

import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    var storedgdgData=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.fragmentContainerView)
        drawerLayout = binding.drawerlayout
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        binding.navView.setupWithNavController(navController)

        binding.appBarMain.menuButton.setOnClickListener {
            binding.drawerlayout.openDrawer(GravityCompat.START)
        }

//        loadConnectionStatus()

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            binding.appBarMain.titleactionbar.text = when (destination.id) {
                R.id.settings -> "SETTINGS"
                R.id.connection -> "CONNECTION"
                R.id.alarm_notification -> "ALARM AND NOTIFICATION"
                R.id.teamDetails -> "TEAM DETAILS"
                R.id.upcomingEvents -> "EVENTS"
                R.id.home -> "HOME"
                R.id.oldGdgList -> "OLD GDG DATA"
                R.id.calender->"EVENTS CALENDAR"
                R.id.gdgChapterDetails -> "CHAPTER DETAIL"
                R.id.oldGdgList -> "OLD GOOGLE DEVELOPER GROUPS"
                else -> "GOOGLE DEVELOPER COMMUNITY VISUALIZATION TOOL"
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
            binding.appBarMain.connectionStatus.text="Connected"
            binding.appBarMain.connectionStatus.setTextColor(Color.parseColor("#52b788"))
        } else {
            binding.appBarMain.connectionStatus.text="Not Connected"
            binding.appBarMain.connectionStatus.setTextColor(Color.parseColor("#ba181b"))

        }
    }



}