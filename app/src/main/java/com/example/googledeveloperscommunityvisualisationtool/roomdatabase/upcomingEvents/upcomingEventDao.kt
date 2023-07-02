package com.example.googledeveloperscommunityvisualisationtool.roomdatabase.upcomingEvents

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.googledeveloperscommunityvisualisationtool.fragments.upcomingEvents.UpcomingEvents


@Dao
interface upcomingEventDao
{

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addEvents(upcomingEventEntity: UpcomingEventEntity)

    @Query("SELECT * FROM upcoming_event_entity Order By start_date")
    fun readAllEvent():LiveData<List<UpcomingEventEntity>>

    @Delete
    suspend fun deleteevent(upcomingEventsEntity: UpcomingEventEntity)

    @Query("DELETE FROM upcoming_event_entity")
    suspend fun deleteallevents()

}