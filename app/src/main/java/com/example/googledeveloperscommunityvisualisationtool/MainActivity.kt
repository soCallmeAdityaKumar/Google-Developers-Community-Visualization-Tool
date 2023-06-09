package com.example.googledeveloperscommunityvisualisationtool

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.ui.AppBarConfiguration
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)




        sharedPref=getSharedPreferences("didShowPrompt", MODE_PRIVATE)
        prefEdit=sharedPref.edit()

        if(!sharedPref.getBoolean("didShowPrompt",false)){
            TapTargetView.showFor(this,
                TapTarget.forView(
                    binding.appBarMain.menuButton,
                    "Nav Bar Menu Button",
                    "This will open The Navigation Drawer for your Navigation to various screen"
                )
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
                        binding.drawerlayout.openDrawer(GravityCompat.START)
                        showNavHometaptarget()
                        super.onTargetClick(view)

                    }
                })
        }

        binding.appBarMain.menuButton.setOnClickListener{
            binding.drawerlayout.openDrawer(GravityCompat.START)
            if(!sharedPref.getBoolean("didShowPrompt",false)){

            }

        }


        //Navigation View
        val navView: NavigationView = binding.navView
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    replaceFragement(Home())
                    binding.drawerlayout.closeDrawer(GravityCompat.START)
                    binding.appBarMain.titleactionbar.text = "Home"

                    true
                }

                R.id.maps -> {
                    replaceFragement(maps())
                    binding.drawerlayout.closeDrawer(GravityCompat.START)
                    binding.appBarMain.titleactionbar.text = "Map"
                    true
                }

                R.id.settings -> {
                    replaceFragement(MainSettings())
                    binding.drawerlayout.closeDrawer(GravityCompat.START)
                    binding.appBarMain.titleactionbar.text = "Settings"
                    true
                }

                R.id.events -> {
                    replaceFragement(UpcomingEvents())
                    binding.drawerlayout.closeDrawer(GravityCompat.START)
                    binding.appBarMain.titleactionbar.text = "Events"
                    true
                }

                R.id.teams -> {
                    replaceFragement(teamDetails())
                    binding.drawerlayout.closeDrawer(GravityCompat.START)
                    binding.appBarMain.titleactionbar.text = "Teams"
                    true
                }

                else -> {
                    replaceFragement(Home())
                    binding.drawerlayout.closeDrawer(GravityCompat.START)
                    binding.appBarMain.titleactionbar.text = "Home"
                    true
                }
            }
        }

    }


    fun showNavHometaptarget(){
        if(!sharedPref.getBoolean("didShowPrompt",false)){
            TapTargetView.showFor(this,
                TapTarget.forView(
                    binding.navView.findViewById(R.id.settings),
                    "Settings ",
                    "First Connect to the Liquid Galaxy Rigs")
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
                        replaceFragement(Settings())
                        binding.drawerlayout.closeDrawer(GravityCompat.START)
                        binding.appBarMain.titleactionbar.text = "Home"
                    }
                })
        }

    }

    private fun replaceFragement(fragment:Fragment){
        val fragmentManager=supportFragmentManager
        val fragmenttransaction=fragmentManager.beginTransaction()
        fragmenttransaction.replace(R.id.frameLayout,fragment)
        fragmenttransaction.commit()

    }

}