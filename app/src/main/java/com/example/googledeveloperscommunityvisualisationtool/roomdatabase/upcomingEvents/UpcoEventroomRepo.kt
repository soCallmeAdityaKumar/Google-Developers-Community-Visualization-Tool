package com.example.googledeveloperscommunityvisualisationtool.roomdatabase.upcomingEvents

import androidx.lifecycle.LiveData

class UpcoEventroomRepo(private val dao: upcomingEventDao)
{

    val readAllEventRepo:LiveData<List<UpcomingEventEntity>> =dao.readAllEvent()
    suspend fun  addEventsRepo(upcomingEventEntity: UpcomingEventEntity)
    {
        dao.addEvents(upcomingEventEntity)
    }

    suspend fun deleteevent(upcomingEventEntity: UpcomingEventEntity){
        dao.deleteevent(upcomingEventEntity)
    }
    suspend fun deleteAllevent(){
        dao.deleteallevents()
    }
}