package com.example.googledeveloperscommunityvisualisationtool.roomdatabase.OldData

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapters.ChapterUrlDatabase
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapters.ChapterUrldatabaseRespository
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapters.ChaptersUrlEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OldGDGdatabaseViewModel(context: Context):ViewModel() {
    val readAllOldGDGViewModel: LiveData<List<OldGDGEntity>>
    private  val repo: oldgdgdatabaseRepository

    init {
        val dao= oldGDGDatabase.getoldGDGDatabase(context).oldchapterDao()
        repo=oldgdgdatabaseRepository(dao)
        readAllOldGDGViewModel=repo.readoldChaptersRepo

    }

    fun addoldGDGViewModel(oldGDGEntity: OldGDGEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repo.addoldgdgRepo(oldGDGEntity)
        }
    }
}