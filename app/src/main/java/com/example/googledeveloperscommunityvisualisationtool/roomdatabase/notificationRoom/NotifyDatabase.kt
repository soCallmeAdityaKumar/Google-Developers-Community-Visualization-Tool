package com.example.googledeveloperscommunityvisualisationtool.roomdatabase.notificationRoom

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [NotifyEntity::class], version = 1, exportSchema = false)
abstract class NotifyDatabase:RoomDatabase(){

    abstract  fun dao(): NotifyDao

    companion object
    {
        @Volatile
        private var INSTANCE: NotifyDatabase?=null

        fun getNotifyDatabase(context: Context): NotifyDatabase
        {
            val tempInstance= INSTANCE
            if(tempInstance!=null)
            {
                return tempInstance
            }
            synchronized(this)
            {
                val instance= Room.databaseBuilder(context.applicationContext,
                    NotifyDatabase::class.java,"notification_database").build()

                INSTANCE =instance
                return instance

            }
        }
    }
}