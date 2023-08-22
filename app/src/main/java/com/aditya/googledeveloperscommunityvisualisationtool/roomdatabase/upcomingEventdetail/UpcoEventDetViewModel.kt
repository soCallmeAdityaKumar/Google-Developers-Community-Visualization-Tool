package com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.upcomingEventdetail

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.upcomingEvents.EventsDatabase
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.upcomingEvents.UpcoEventroomRepo
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.upcomingEvents.UpcomingEventEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpcoEventDetViewModel( context: Context): ViewModel() {
    val readAllEventViewModel: LiveData<List<UpcoEventDetEntity>>
    private  val repo: UpcoEventDetRepo

    init {
        val dao= UpcoEventDetDatabase.getEventDatabase(context).eventDao()
        repo= UpcoEventDetRepo(dao)
        readAllEventViewModel=repo.readAllEventRepo
    }

    fun addEventViewModel(event: UpcoEventDetEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repo.addEventsRepo(event)
        }
    }

    fun deleteevent(upcoEventDetEntity: UpcoEventDetEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteevent(upcoEventDetEntity)
        }
    }
    fun deleteAllevent(){
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteAllevent()
        }
    }
}