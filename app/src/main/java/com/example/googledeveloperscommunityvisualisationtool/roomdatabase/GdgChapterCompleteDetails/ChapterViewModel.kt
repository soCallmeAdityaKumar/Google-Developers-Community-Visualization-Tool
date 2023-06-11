package com.example.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapterCompleteDetails

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChapterViewModel(context: Context):ViewModel() {
    val readAllChaptersViewModel: LiveData<List<ChapterEntity>>
    private  val repo: ChapterRespository
    init {
        val dao= ChapterDatabase.chapterDatabase(context).chapterDao()
        repo= ChapterRespository(dao)
        readAllChaptersViewModel=repo.readChapterRepo
    }
    fun addChaptersViewModel(chapterEntity: ChapterEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repo.addChaptersRepo(chapterEntity)
        }
    }

}