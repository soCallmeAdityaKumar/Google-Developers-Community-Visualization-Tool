package com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.LastWeekDatabase

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class lastweekroommodel(context: Context): ViewModel() {
    val readlastweekEventViewModel: LiveData<List<weekEventEntity>>
    private  val repo: lastweekroomRepo

    init {
        val dao= lastweekDatabase.getEventDatabase(context).lastweekeventDao()
        repo= lastweekroomRepo(dao)
        readlastweekEventViewModel=repo.readAllEventRepo
    }

    fun addEventViewModel(event: weekEventEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repo.addEventsRepo(event)
        }
    }
    fun deleteAlllastWeekViewModel(){
        viewModelScope.launch(Dispatchers.IO){
            repo.deleteLastWeekroom()
        }
    }
}