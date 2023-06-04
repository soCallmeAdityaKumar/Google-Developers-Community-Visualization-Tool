package com.example.googledeveloperscommunityvisualisationtool

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.googledeveloperscommunityvisualisationtool.Repository.GdgScrapingRespository
import com.example.googledeveloperscommunityvisualisationtool.Repository.UpcomingEventRepository
import com.example.googledeveloperscommunityvisualisationtool.ViewModel.UpcomingEventViewModel
import com.example.googledeveloperscommunityvisualisationtool.ViewModel.UpcomingEventViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var textView1:TextView
    lateinit var upcomingEventViewModel:UpcomingEventViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView1=findViewById(R.id.textView1)

        val upcomingEventRepository=UpcomingEventRepository(this)

        upcomingEventViewModel=ViewModelProvider(this,UpcomingEventViewModelFactory(upcomingEventRepository)).get(UpcomingEventViewModel::class.java)

        CoroutineScope(Dispatchers.IO).launch{
            upcomingEventViewModel.getResponseViewModel()
        }

        var gdgScrapingRespository=GdgScrapingRespository()
        CoroutineScope(Dispatchers.IO).launch {
            gdgScrapingRespository.scrape()
        }
    }
}