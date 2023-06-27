package com.example.googledeveloperscommunityvisualisationtool

import android.content.SharedPreferences
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

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var sharedPref:SharedPreferences
    lateinit var prefEdit:SharedPreferences.Editor
    private lateinit var navController: NavController
    private lateinit var drawerLayout:DrawerLayout
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

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            binding.appBarMain.titleactionbar.text = when (destination.id) {
                R.id.settings -> "SETTINGS"
                R.id.connection -> "CONNECTION"
                R.id.alarm_notification -> "ALARM AND NOTIFICATION"
                R.id.teamDetails -> "TEAM DETAILS"
                R.id.upcomingEvents -> "EVENTS"
                R.id.home -> "HOME"
                R.id.oldGdgList -> "OLD GDG DATA"
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



}