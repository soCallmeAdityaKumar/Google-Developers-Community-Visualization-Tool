package com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.upcomingEventdetail

import androidx.lifecycle.LiveData
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.upcomingEvents.UpcomingEventEntity
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.upcomingEvents.upcomingEventDao

class UpcoEventDetRepo(private val dao: UpcoEventDetDao) {
    val readAllEventRepo: LiveData<List<UpcoEventDetEntity>> =dao.readAllEvent()
    suspend fun  addEventsRepo(upcoEventDetEntity: UpcoEventDetEntity)
    {
        dao.addEvents(upcoEventDetEntity)
    }

    suspend fun deleteevent(upcoEventDetEntity: UpcoEventDetEntity){
        dao.deleteevent(upcoEventDetEntity)
    }
    suspend fun deleteAllevent(){
        dao.deleteallevents()
    }
}