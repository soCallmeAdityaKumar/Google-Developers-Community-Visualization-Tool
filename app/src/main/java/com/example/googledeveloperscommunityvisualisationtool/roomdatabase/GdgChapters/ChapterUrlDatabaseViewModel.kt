package com.example.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapters

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChapterUrlDatabaseViewModel(context: Context):ViewModel() {
    val readAllChapterUrlViewModel: LiveData<List<ChaptersUrlEntity>>
    private  val repo: ChapterUrldatabaseRespository

    init {
        val dao= ChapterUrlDatabase.getChapterDatabase(context).chapterDao()
        repo= ChapterUrldatabaseRespository(dao)
        readAllChapterUrlViewModel=repo.readChapterurlRepo

    }

    fun addChapterUrlViewModel(chapter: ChaptersUrlEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repo.addChapterUrlRepo(chapter)
        }
    }

}