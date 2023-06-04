package com.example.googledeveloperscommunityvisualisationtool.roomdatabase

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.googledeveloperscommunityvisualisationtool.DataClass.Volley.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class upcomingEventdatabaseViewModel(application: Application):ViewModel() {

    private  val readAllEventViewModel:LiveData<List<Result>>
    private  val repo:upcomingEventdatabaseRepository

    init {
        val dao=EventsDatabase.getEventDatabase(application).eventDao()
        repo= upcomingEventdatabaseRepository(dao)
        readAllEventViewModel=repo.readAllEventRepo
    }

    fun addEventViewModel(event:Result){
        viewModelScope.launch(Dispatchers.IO) {
            repo.addEventsRepo(event)
        }
    }
}