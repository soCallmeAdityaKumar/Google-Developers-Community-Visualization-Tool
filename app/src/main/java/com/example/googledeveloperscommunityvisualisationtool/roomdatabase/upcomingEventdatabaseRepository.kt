package com.example.googledeveloperscommunityvisualisationtool.roomdatabase

import androidx.lifecycle.LiveData
import com.example.googledeveloperscommunityvisualisationtool.DataClass.Volley.Result

class upcomingEventdatabaseRepository(private val dao:upcomingEventDao)
{

    val readAllEventRepo:LiveData<List<Result>> =dao.readAllEvent()
    suspend fun  addEventsRepo(result: Result)
    {
        dao.addEvents(result)
    }
}