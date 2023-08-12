package com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.upcomingEvents

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpcoEventroomViewmodel(context: Context):ViewModel() {

      val readAllEventViewModel:LiveData<List<UpcomingEventEntity>>
    private  val repo: UpcoEventroomRepo

    init {
        val dao= EventsDatabase.getEventDatabase(context).eventDao()
        repo= UpcoEventroomRepo(dao)
        readAllEventViewModel=repo.readAllEventRepo
    }

    fun addEventViewModel(event: UpcomingEventEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repo.addEventsRepo(event)
        }
    }

    fun deleteevent(upcomingEventEntity: UpcomingEventEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteevent(upcomingEventEntity)
        }
    }
    fun deleteAllevent(){
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteAllevent()
        }
    }
}