package com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.notificationRoom

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotifyViewModel(val context: Context): ViewModel() {
    val readAllNotification: LiveData<List<NotifyEntity>>
    private  val repo: NotifyRepository

    init {
        val dao= NotifyDatabase.getNotifyDatabase(context).dao()
        repo= NotifyRepository(dao)
        readAllNotification=repo.readNotifydata

    }

    fun addNotificationViewModel(notifyEntity: NotifyEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repo.addNotification(notifyEntity)
        }
    }
    fun deleteAllNotification(){
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteAllNotification()
        }
    }
}