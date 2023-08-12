package com.aditya.googledeveloperscommunityvisualisationtool.dataFetching.oldData.oldGdg

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.ViewModel

class OldGdgListViewModel(val oldgdgRepo:OldGdgRepository,val context: Context) : ViewModel() {

    suspend fun getGdgdata(){
        oldgdgRepo.getoldGdgdata()
    }
    fun returnlist():List<oldGdgDataItem>{
        return oldgdgRepo.returnoldGdgItem()
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