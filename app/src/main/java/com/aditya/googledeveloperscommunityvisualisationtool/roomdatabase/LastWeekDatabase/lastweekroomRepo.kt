package com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.LastWeekDatabase

import androidx.lifecycle.LiveData
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.upcomingEvents.UpcomingEventEntity

class lastweekroomRepo(val dao:lastWeekDao) {
    val readAllEventRepo: LiveData<List<weekEventEntity>> =dao.readlastweekEvent()
    suspend fun  addEventsRepo(WeekEventEntity: weekEventEntity)
    {
        dao.addlastweekEvents(WeekEventEntity)
    }

    suspend fun deleteLastWeekroom(){
        dao.deleteAllLastweek()
    }
}