package com.example.googledeveloperscommunityvisualisationtool.roomdatabase.lastWeekEvent

import androidx.lifecycle.LiveData
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.upcomingEvents.UpcomingEventEntity

class LastEventRepo(val dao:LastEventDao) {
    val readAllEventRepo: LiveData<List<LastEventEntity>> =dao.readAllEvent()
    suspend fun  addEventsRepo(lastEventEntity: LastEventEntity)
    {
        dao.addEvents(lastEventEntity)
    }

    suspend fun deleteevent(lastEventEntity: LastEventEntity){
        dao.deleteevent(lastEventEntity)
    }
    suspend fun deleteAllevent(){
        dao.deleteallevents()
    }
}