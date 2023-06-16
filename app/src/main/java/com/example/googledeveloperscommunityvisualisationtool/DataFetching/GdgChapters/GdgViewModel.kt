package com.example.googledeveloperscommunityvisualisationtool.DataFetching.GdgChapters

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.ViewModel
import com.example.googledeveloperscommunityvisualisationtool.DataClass.Scraping.GdgGroupClasses.GdgGroupDataClassItem
import com.example.googledeveloperscommunityvisualisationtool.DataFetching.UpcomingEvents.UpcomingEventViewModel
import com.example.googledeveloperscommunityvisualisationtool.Fragments.Home.GDGDetails

class GdgViewModel(val gdgChaptersRepo:GdgScrapingRespository,val context:Context): ViewModel() {
    private  var chapters=ArrayList<GdgGroupDataClassItem>()
    private lateinit var gdgDetails:GDGDetails
    fun getChaptersViewModel(){
        gdgChaptersRepo.getGdgChapters()
    }
    fun returnGDGChapterViewModel():ArrayList<GdgGroupDataClassItem>{
        chapters=gdgChaptersRepo.returnchapter()
        return chapters
    }
    suspend fun getCompleteGDGdetails(url:String){
         gdgDetails=gdgChaptersRepo.getResponse(url)
    }
    fun getdetails():GDGDetails{
        return gdgDetails
    }
    fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }
}