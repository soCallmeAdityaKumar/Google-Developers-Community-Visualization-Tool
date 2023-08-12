package com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.lastWeekEvent

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LastEventViewModel( context: Context):ViewModel() {
    val readAllEventViewModel: LiveData<List<LastEventEntity>>
    private val repo: LastEventRepo

    init {
        val dao = LastEventDatabase.getEventDatabase(context).lastEventDaoFunc()
        repo = LastEventRepo(dao)
        readAllEventViewModel = repo.readAllEventRepo
    }

    fun addEventViewModel(event: LastEventEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.addEventsRepo(event)
        }
    }

    fun deleteevent(lastEventEntity: LastEventEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteevent(lastEventEntity)
        }
    }

    fun deleteAllevent() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteAllevent()
        }
    }
}