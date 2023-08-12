package com.aditya.googledeveloperscommunityvisualisationtool.dataFetching.upcomingEvents

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.ViewModel
import com.aditya.googledeveloperscommunityvisualisationtool.dataClass.volley.Result

class UpcoEventViewMod(val repository: UpcomEventRepo, val context:Context):ViewModel() {

      var eventList= mutableListOf<Result>()
      suspend fun getResponseViewModel(){
        repository.getResponse()
    }
    fun returnlistViewModel():MutableList<Result>{
         eventList=repository.returnEventList()
        Log.d("viewModelList",eventList.size.toString())
        return eventList

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