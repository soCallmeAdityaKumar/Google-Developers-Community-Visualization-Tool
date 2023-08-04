package com.example.googledeveloperscommunityvisualisationtool.roomdatabase.lastWeekEvent

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.upcomingEvents.UpcomingEventEntity

@Dao
interface LastEventDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addEvents(lastEventEntity: LastEventEntity)

    @Query("SELECT * FROM last_week_event_details Order By id")
    fun readAllEvent(): LiveData<List<LastEventEntity>>

    @Delete
    suspend fun deleteevent(lastEventEntity: LastEventEntity)

    @Query("DELETE FROM last_week_event_details")
    suspend fun deleteallevents()
}