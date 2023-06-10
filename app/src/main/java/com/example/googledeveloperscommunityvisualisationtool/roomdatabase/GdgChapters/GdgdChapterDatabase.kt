package com.example.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapters

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.UpcomingEvents.EventsDatabase
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.UpcomingEvents.UpcomingEventEntity
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.UpcomingEvents.upcomingEventDao

@Database(entities = [GdgChaptersEntity::class], version = 1, exportSchema = false)
abstract class GdgdChapterDatabase:RoomDatabase() {
    abstract  fun chapterDao(): GdgChapterDao

    companion object
    {
        @Volatile
        private var INSTANCE: GdgdChapterDatabase?=null

        fun getChapterDatabase(context: Context): GdgdChapterDatabase
        {
            val tempInstance= INSTANCE
            if(tempInstance!=null)
            {
                return tempInstance
            }
            synchronized(this)
            {
                val instance= Room.databaseBuilder(context.applicationContext,
                    GdgdChapterDatabase::class.java,"chapter_database").build()

                INSTANCE =instance
                return instance

            }
        }
    }
}