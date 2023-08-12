package com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.lastWeekEvent

import androidx.lifecycle.LiveData

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