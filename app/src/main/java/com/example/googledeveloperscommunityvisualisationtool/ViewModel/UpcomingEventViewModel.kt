package com.example.googledeveloperscommunityvisualisationtool.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.android.volley.Response
import com.example.googledeveloperscommunityvisualisationtool.DataClass.Volley.Result
import com.example.googledeveloperscommunityvisualisationtool.Repository.UpcomingEventRepository

class UpcomingEventViewModel(val repository: UpcomingEventRepository):ViewModel() {
      fun getResponseViewModel():MutableList<Result>{
        return repository.getResponse()
    }

}