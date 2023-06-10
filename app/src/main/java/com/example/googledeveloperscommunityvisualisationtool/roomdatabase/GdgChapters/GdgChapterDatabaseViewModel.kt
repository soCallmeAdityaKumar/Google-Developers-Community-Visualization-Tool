package com.example.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapters

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.UpcomingEvents.EventsDatabase
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.UpcomingEvents.UpcomingEventEntity
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.UpcomingEvents.UpcomingEventdatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GdgChapterDatabaseViewModel(context: Context):ViewModel() {
    val readAllChapterViewModel: LiveData<List<GdgChaptersEntity>>
    private  val repo: GdgChapeterdatabaseRespository

    init {
        val dao= GdgdChapterDatabase.getChapterDatabase(context).chapterDao()
        repo= GdgChapeterdatabaseRespository(dao)
        readAllChapterViewModel=repo.readAllChapterRepo
    }

    fun addChapterViewModel(chapter: GdgChaptersEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repo.addChapterRepo(chapter)
        }
    }
}