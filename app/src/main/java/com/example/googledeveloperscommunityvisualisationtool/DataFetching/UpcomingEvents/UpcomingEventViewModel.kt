package com.example.googledeveloperscommunityvisualisationtool.DataFetching.UpcomingEvents

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.googledeveloperscommunityvisualisationtool.DataClass.Volley.Result
import com.example.googledeveloperscommunityvisualisationtool.DataFetching.UpcomingEvents.UpcomingEventRepository

class UpcomingEventViewModel(val repository: UpcomingEventRepository, val context:Context):ViewModel() {

      var eventList= mutableListOf<Result>()
      fun getResponseViewModel(){
        repository.getResponse()
    }
    fun returnlistViewModel():MutableList<Result>{
        val eventList=repository.returnEventList()
        Log.d("viewModelList",eventList.size.toString())
        return repository.returnEventList()

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