package com.aditya.googledeveloperscommunityvisualisationtool.fragments.upcomingEvents.detailsUpcomingEvents

import android.content.Context
import androidx.lifecycle.ViewModel
import com.aditya.googledeveloperscommunityvisualisationtool.fragments.upcomingEvents.detailsUpcomingEvents.dataItem.upcomEventData

class UpcoEventDetailsModel(val repo:upcoEventsDetailsRepo) : ViewModel() {
    fun getResponseModel(baseurl:String,context: Context){
        repo.getResponse(baseurl,context)
    }
    fun returnEvents():upcomEventData{
        return repo.returndetails()
    }
}