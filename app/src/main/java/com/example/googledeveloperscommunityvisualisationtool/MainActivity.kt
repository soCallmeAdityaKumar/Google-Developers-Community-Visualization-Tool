package com.example.googledeveloperscommunityvisualisationtool

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentController
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.googledeveloperscommunityvisualisationtool.Fragments.Maps.maps
import com.example.googledeveloperscommunityvisualisationtool.Fragments.Settings.MainSettings
import com.example.googledeveloperscommunityvisualisationtool.Fragments.Settings.Settings
import com.example.googledeveloperscommunityvisualisationtool.Fragments.TeamDetails.teamDetails
import com.example.googledeveloperscommunityvisualisationtool.Fragments.UpcomingEvents.Home
import com.example.googledeveloperscommunityvisualisationtool.Fragments.UpcomingEvents.UpcomingEvents
import com.example.googledeveloperscommunityvisualisationtool.databinding.ActivityMainBinding
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetView
import com.google.android.material.navigation.NavigationView

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

        navController=findNavController(R.id.fragmentContainerView)
        drawerLayout=binding.drawerlayout
        appBarConfiguration= AppBarConfiguration(navController.graph,drawerLayout)
        binding.navView.setupWithNavController(navController)

        binding.appBarMain.menuButton.setOnClickListener{
            binding.drawerlayout.openDrawer(GravityCompat.START)
        }

        navController.addOnDestinationChangedListener{controller,destination,arguments->
            binding.appBarMain.titleactionbar.text=when(destination.id){
                R.id.settings -> "SETTINGS"
                R.id.connection -> "CONNECTION"
                R.id.alarm_notification -> "ALARM AND NOTIFICATION"
                R.id.teamDetails -> "TEAM DETAILS"
                R.id.upcomingEvents -> "EVENTS"
                R.id.home -> "HOME"
                R.id.oldGdgList->"OLD GDG DATA"
                R.id.gdgChapterDetails->"CHAPTER DETAIL"
                R.id.oldGdgList->"OLD GOOGLE DEVELOPER GROUPS"
                else -> "GOOGLE DEVELOPER COMMUNITY VISUALIZATION TOOL"
            }

        }

        sharedPref=getSharedPreferences("didShowPrompt", MODE_PRIVATE)
        prefEdit=sharedPref.edit()

//        if(!sharedPref.getBoolean("didShowPrompt",false)){
//            TapTargetView.showFor(this,
//                TapTarget.forView(
//                    binding.appBarMain.menuButton,
//                    "Nav Bar Menu Button",
//                    "This will open The Navigation Drawer for your Navigation to various screen"
//                )
//                    .outerCircleColor(androidx.appcompat.R.color.material_deep_teal_200)
//                    .outerCircleAlpha(0.96f)
//                    .targetCircleColor(R.color.white)
//                    .titleTextSize(20)
//                    .titleTextColor(R.color.white)
//                    .descriptionTextSize(10)
//                    .descriptionTextColor(R.color.black)
//                    .textColor(R.color.black)
//                    .textTypeface(Typeface.SANS_SERIF)
//                    .dimColor(R.color.black)
//                    .drawShadow(true)
//                    .cancelable(false)
//                    .tintTarget(true)
//                    .transparentTarget(true)
//                    .targetRadius(60),
//                object : TapTargetView.Listener() {
//                    override fun onTargetClick(view: TapTargetView?) {
//                        binding.drawerlayout.openDrawer(GravityCompat.START)
//                        showNavHometaptarget()
//                        super.onTargetClick(view)
//
//                    }
//                })
//        }

//        binding.appBarMain.menuButton.setOnClickListener{
//            binding.drawerlayout.openDrawer(GravityCompat.START)
//            if(!sharedPref.getBoolean("didShowPrompt",false)){
//
//            }
//
//        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController=findNavController(R.id.fragmentContainerView)
        return navController.navigateUp(appBarConfiguration)|| super.onSupportNavigateUp()
    }


//    fun showNavHometaptarget(){
//        if(!sharedPref.getBoolean("didShowPrompt",false)){
//            TapTargetView.showFor(this,
//                TapTarget.forView(
//                    binding.navView.findViewById(R.id.settings),
//                    "Settings ",
//                    "First Connect to the Liquid Galaxy Rigs")
//                    .outerCircleColor(androidx.appcompat.R.color.material_deep_teal_200)
//                    .outerCircleAlpha(0.96f)
//                    .targetCircleColor(R.color.white)
//                    .titleTextSize(20)
//                    .titleTextColor(R.color.white)
//                    .descriptionTextSize(10)
//                    .descriptionTextColor(R.color.black)
//                    .textColor(R.color.black)
//                    .textTypeface(Typeface.SANS_SERIF)
//                    .dimColor(R.color.black)
//                    .drawShadow(true)
//                    .cancelable(false)
//                    .tintTarget(true)
//                    .transparentTarget(true)
//                    .targetRadius(60),
//                object : TapTargetView.Listener() {
//                    override fun onTargetClick(view: TapTargetView?) {
//                        super.onTargetClick(view)
//                        replaceFragement(Settings())
//                        binding.drawerlayout.closeDrawer(GravityCompat.START)
//                        binding.appBarMain.titleactionbar.text = "Home"
//                    }
//                })
//        }
//
//    }



}