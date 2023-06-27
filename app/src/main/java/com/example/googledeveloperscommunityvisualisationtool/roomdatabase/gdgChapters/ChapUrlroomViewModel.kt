package com.example.googledeveloperscommunityvisualisationtool.roomdatabase.gdgChapters

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChapUrlroomViewModel(context: Context):ViewModel() {
    val readAllChapterUrlViewModel: LiveData<List<ChaptersUrlEntity>>
    private  val repo: ChapUrlroomRespo

    init {
        val dao= ChapterUrlDatabase.getChapterDatabase(context).chapterDao()
        repo= ChapUrlroomRespo(dao)
        readAllChapterUrlViewModel=repo.readChapterurlRepo

    }

    fun addChapterUrlViewModel(chapter: ChaptersUrlEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repo.addChapterUrlRepo(chapter)
        }
    }

}