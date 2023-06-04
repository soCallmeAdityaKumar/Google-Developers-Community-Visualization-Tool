package com.example.googledeveloperscommunityvisualisationtool

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import com.example.googledeveloperscommunityvisualisationtool.Repository.GdgScrapingRespository
import com.example.googledeveloperscommunityvisualisationtool.Repository.UpcomingEventRepository
import com.example.googledeveloperscommunityvisualisationtool.ViewModel.UpcomingEventViewModel
import com.example.googledeveloperscommunityvisualisationtool.ViewModel.UpcomingEventViewModelFactory
import com.example.googledeveloperscommunityvisualisationtool.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding=ActivityMainBinding.inflate(layoutInflater)

        var gdgScrapingRespository=GdgScrapingRespository()
        CoroutineScope(Dispatchers.IO).launch {
            gdgScrapingRespository.scrape()
        }

        val drawerLayout: DrawerLayout =binding.drawerlayout
        val navView:NavigationView=binding.navView

//        navView.setNavigationItemSelectedListener {
//
//        }


    }
}