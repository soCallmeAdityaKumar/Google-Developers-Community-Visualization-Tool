package com.aditya.googledeveloperscommunityvisualisationtool.dataFetching.oldData.oldEvents

import android.content.Context
import androidx.lifecycle.ViewModel
import com.aditya.googledeveloperscommunityvisualisationtool.dataFetching.oldData.oldEvents.oldEventsDataClass.oldEventsData

class OldEventViewModel(val repo:oldEventRepository, context: Context): ViewModel() {
//    private var oldevents:List<oldEventsData>
    fun getResponse(endpoint:String){
        repo.getResponse(endpoint)
    }
    fun getOldEventsViewModel():oldEventsData{
        return repo.event
    }

}