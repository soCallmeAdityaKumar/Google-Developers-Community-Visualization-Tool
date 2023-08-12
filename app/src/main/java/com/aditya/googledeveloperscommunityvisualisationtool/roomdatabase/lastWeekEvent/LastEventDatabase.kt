package com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.lastWeekEvent

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [LastEventEntity::class], version = 1, exportSchema = false)
abstract class LastEventDatabase:RoomDatabase() {
     abstract  fun lastEventDaoFunc(): LastEventDao

    companion object
    {
        @Volatile
        private var INSTANCE:LastEventDatabase ?=null

        fun getEventDatabase(context: Context): LastEventDatabase
        {
            val tempInstance= INSTANCE
            if(tempInstance!=null)
            {
                return tempInstance
            }
            synchronized(this)
            {
                val instance= Room.databaseBuilder(context.applicationContext,
                    LastEventDatabase::class.java,"last_event_database").build()

                INSTANCE =instance
                return instance


            }
        }
    }
}