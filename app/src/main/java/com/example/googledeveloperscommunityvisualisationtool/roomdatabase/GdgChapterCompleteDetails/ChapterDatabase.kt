package com.example.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapterCompleteDetails

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ChapterEntity::class], version = 1, exportSchema = false)
abstract class ChapterDatabase:RoomDatabase() {
    abstract  fun chapterDao(): ChapterDao

    companion object
    {
        @Volatile
        private var INSTANCE: ChapterDatabase?=null

        fun chapterDatabase(context: Context): ChapterDatabase
        {
            val tempInstance= INSTANCE
            if(tempInstance!=null)
            {
                return tempInstance
            }
            synchronized(this)
            {
                val instance= Room.databaseBuilder(context.applicationContext,
                    ChapterDatabase::class.java,"chapter_database").build()

                INSTANCE =instance
                return instance

            }
        }
    }
}