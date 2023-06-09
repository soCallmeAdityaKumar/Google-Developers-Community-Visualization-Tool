package com.example.googledeveloperscommunityvisualisationtool.roomdatabase

import androidx.lifecycle.LiveData

class UpcomingEventdatabaseRepository(private val dao:upcomingEventDao)
{

    val readAllEventRepo:LiveData<List<UpcomingEventEntity>> =dao.readAllEvent()
    suspend fun  addEventsRepo(upcomingEventEntity: UpcomingEventEntity)
    {
        dao.addEvents(upcomingEventEntity)
    }
}