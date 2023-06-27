package com.example.googledeveloperscommunityvisualisationtool.dataFetching.gdgChapters

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.ViewModel
import com.example.googledeveloperscommunityvisualisationtool.dataClass.gdgGroupClasses.GdgDataClass
import com.example.googledeveloperscommunityvisualisationtool.fragments.home.GDGDetails

class GdgViewModel(val gdgChaptersRepo:GdgScrapingRespo, val context:Context): ViewModel() {
    private  var chapters=ArrayList<GdgDataClass>()
    private lateinit var gdgDetails:GDGDetails
    fun getChaptersViewModel(){
        gdgChaptersRepo.getGdgChapters()
    }
    fun returnGDGChapterViewModel():ArrayList<GdgDataClass>{
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
        return false
    }
}