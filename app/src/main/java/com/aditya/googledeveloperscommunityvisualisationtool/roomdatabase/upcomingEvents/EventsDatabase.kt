package com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.upcomingEvents

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UpcomingEventEntity::class], version = 1, exportSchema = false)
abstract  class EventsDatabase:RoomDatabase()
{

    abstract  fun eventDao(): upcomingEventDao

    companion object
    {
        @Volatile
        private var INSTANCE: EventsDatabase?=null

        fun getEventDatabase(context: Context): EventsDatabase
        {
            val tempInstance= INSTANCE
            if(tempInstance!=null)
            {
                return tempInstance
            }
            synchronized(this)
            {
                val instance= Room.databaseBuilder(context.applicationContext,
                    EventsDatabase::class.java,"event_database").build()

                INSTANCE =instance
                return instance


            }
        }
    }

}
