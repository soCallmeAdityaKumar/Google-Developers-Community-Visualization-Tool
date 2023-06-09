package com.example.googledeveloperscommunityvisualisationtool.roomdatabase

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpcomingEventdatabaseViewModel(context: Context):ViewModel() {

      val readAllEventViewModel:LiveData<List<UpcomingEventEntity>>
    private  val repo:UpcomingEventdatabaseRepository

    init {
        val dao=EventsDatabase.getEventDatabase(context).eventDao()
        repo= UpcomingEventdatabaseRepository(dao)
        readAllEventViewModel=repo.readAllEventRepo
    }

    fun addEventViewModel(event:UpcomingEventEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repo.addEventsRepo(event)
        }
    }
}