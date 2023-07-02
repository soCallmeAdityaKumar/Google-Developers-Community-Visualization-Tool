package com.example.googledeveloperscommunityvisualisationtool.roomdatabase.LastWeekDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.upcomingEvents.EventsDatabase
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.upcomingEvents.UpcomingEventEntity
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.upcomingEvents.upcomingEventDao

@Database(entities = [weekEventEntity::class], version = 1, exportSchema = false)
abstract class lastweekDatabase:RoomDatabase() {
    abstract  fun lastweekeventDao(): lastWeekDao

    companion object
    {
        @Volatile
        private var INSTANCE: lastweekDatabase?=null

        fun getEventDatabase(context: Context): lastweekDatabase
        {
            val tempInstance= INSTANCE
            if(tempInstance!=null)
            {
                return tempInstance
            }
            synchronized(this)
            {
                val instance= Room.databaseBuilder(context.applicationContext,
                    lastweekDatabase::class.java,"last_week_events").build()

                INSTANCE =instance
                return instance
            }
        }
    }

}