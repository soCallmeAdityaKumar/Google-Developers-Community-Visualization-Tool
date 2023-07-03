package com.example.googledeveloperscommunityvisualisationtool.roomdatabase.LastWeekDatabase

import androidx.lifecycle.LiveData
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.upcomingEvents.UpcomingEventEntity

class lastweekroomRepo(val dao:lastWeekDao) {
    val readAllEventRepo: LiveData<List<weekEventEntity>> =dao.readlastweekEvent()
    suspend fun  addEventsRepo(WeekEventEntity: weekEventEntity)
    {
        dao.addlastweekEvents(WeekEventEntity)
    }
}