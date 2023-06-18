package com.example.googledeveloperscommunityvisualisationtool.DataFetching.OldData.OldEvents

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.googledeveloperscommunityvisualisationtool.DataFetching.OldData.OldEvents.OldEventsDataClass.oldEventsData

class OldEventViewModel( val repo:OldEventRepository, context: Context): ViewModel() {
//    private var oldevents:List<oldEventsData>
    fun getResponse(endpoint:String){
        repo.getResponse(endpoint)
    }
    fun getOldEventsViewModel():oldEventsData{
        return repo.event
    }

}