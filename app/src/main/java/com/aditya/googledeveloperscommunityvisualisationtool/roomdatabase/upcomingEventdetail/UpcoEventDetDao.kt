package com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.upcomingEventdetail

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.upcomingEvents.UpcomingEventEntity

@Dao
interface UpcoEventDetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addEvents(upcoEventDetEntity: UpcoEventDetEntity)

    @Query("SELECT * FROM upco_event_detials Order By date")
    fun readAllEvent(): LiveData<List<UpcoEventDetEntity>>

    @Delete
    suspend fun deleteevent(upcoEventDetEntity: UpcoEventDetEntity)

    @Query("DELETE FROM upco_event_detials")
    suspend fun deleteallevents()
}