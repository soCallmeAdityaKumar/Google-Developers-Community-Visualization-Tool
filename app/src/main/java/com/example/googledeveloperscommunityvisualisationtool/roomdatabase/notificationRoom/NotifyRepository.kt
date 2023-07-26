package com.example.googledeveloperscommunityvisualisationtool.roomdatabase.notificationRoom

import androidx.lifecycle.LiveData

class NotifyRepository(val dao:NotifyDao) {
    val readNotifydata: LiveData<List<NotifyEntity>> =dao.readNotification()

    suspend fun  addNotification(notifyEntity: NotifyEntity)
    {
        dao.addNotification(notifyEntity)
    }
}