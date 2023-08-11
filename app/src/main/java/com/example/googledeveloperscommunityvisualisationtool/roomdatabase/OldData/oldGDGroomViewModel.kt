package com.example.googledeveloperscommunityvisualisationtool.roomdatabase.OldData

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class oldGDGroomViewModel(context: Context):ViewModel() {
    val readAllOldGDGViewModel: LiveData<List<OldGDGEntity>>
    private  val repo: oldgdgroomRepo

    init {
        val dao= oldGDGDatabase.getoldGDGDatabase(context).oldchapterDao()
        repo=oldgdgroomRepo(dao)
        readAllOldGDGViewModel=repo.readoldChaptersRepo

    }

    fun addoldGDGViewModel(oldGDGEntity: OldGDGEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repo.addoldgdgRepo(oldGDGEntity)
        }
    }
    fun deleteAllOldGDGChapterModel(){
        viewModelScope.launch (Dispatchers.IO){
            repo.deleteAllOldGDG()
        }
    }
}