package com.example.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapters

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ChaptersUrlEntity::class], version = 1, exportSchema = false)
abstract class ChapterUrlDatabase:RoomDatabase() {
    abstract  fun chapterDao(): GdgChapterUrlDao

    companion object
    {
        @Volatile
        private var INSTANCE: ChapterUrlDatabase?=null

        fun getChapterDatabase(context: Context): ChapterUrlDatabase
        {
            val tempInstance= INSTANCE
            if(tempInstance!=null)
            {
                return tempInstance
            }
            synchronized(this)
            {
                val instance= Room.databaseBuilder(context.applicationContext,
                    ChapterUrlDatabase::class.java,"chapter_url_database").build()

                INSTANCE =instance
                return instance

            }
        }
    }
}