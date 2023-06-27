package com.example.googledeveloperscommunityvisualisationtool.fragments.upcomingEvents.detailsUpcomingEvents

import androidx.lifecycle.ViewModel
import com.example.googledeveloperscommunityvisualisationtool.fragments.upcomingEvents.detailsUpcomingEvents.dataItem.upcomEventData

class UpcoEventDetailsModel(val repo:upcoEventsDetailsRepo) : ViewModel() {
    fun getResponseModel(baseurl:String){
        repo.getResponse(baseurl)
    }
    fun returnEvents():upcomEventData{
        return repo.returndetails()
    }
}