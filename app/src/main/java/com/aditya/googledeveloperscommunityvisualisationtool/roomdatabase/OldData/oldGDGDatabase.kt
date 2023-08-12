package com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.OldData

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [OldGDGEntity::class], version = 1, exportSchema = false)
abstract class oldGDGDatabase:RoomDatabase() {
    abstract  fun oldchapterDao(): OldGDGdao

    companion object
    {
        @Volatile
        private var INSTANCE:oldGDGDatabase ?=null

        fun getoldGDGDatabase(context: Context): oldGDGDatabase
        {
            val tempInstance= INSTANCE
            if(tempInstance!=null)
            {
                return tempInstance
            }
            synchronized(this)
            {
                val instance= Room.databaseBuilder(context.applicationContext,
                    oldGDGDatabase::class.java,"old_chapter_database").build()

                INSTANCE =instance
                return instance

            }
        }
    }
}