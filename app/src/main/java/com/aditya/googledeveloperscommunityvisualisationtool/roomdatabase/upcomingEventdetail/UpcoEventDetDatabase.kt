package com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.upcomingEventdetail

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.upcomingEvents.EventsDatabase
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.upcomingEvents.upcomingEventDao

@Database(entities = [UpcoEventDetEntity::class], version = 1, exportSchema = false)
abstract class UpcoEventDetDatabase :RoomDatabase(){
    abstract  fun eventDao(): UpcoEventDetDao

    companion object
    {
        @Volatile
        private var INSTANCE: UpcoEventDetDatabase?=null

        fun getEventDatabase(context: Context): UpcoEventDetDatabase
        {
            val tempInstance= INSTANCE
            if(tempInstance!=null)
            {
                return tempInstance
            }
            synchronized(this)
            {
                val instance= Room.databaseBuilder(context.applicationContext,
                    UpcoEventDetDatabase::class.java,"upco_event_det_database").build()

                INSTANCE =instance
                return instance


            }
        }
    }

}