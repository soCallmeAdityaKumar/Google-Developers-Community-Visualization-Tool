package com.aditya.googledeveloperscommunityvisualisationtool.fragments.gdgChapterDetails

import com.aditya.googledeveloperscommunityvisualisationtool.fragments.home.Organizers
import com.aditya.googledeveloperscommunityvisualisationtool.fragments.home.PastEvents
import com.aditya.googledeveloperscommunityvisualisationtool.fragments.home.UpcomingEvents

data class tourGDGDataclass(
    var banner:String,
    var gdgName:String,
    var about:String,
    var lat:Double,
    var lon:Double,
    var cityName:String,
    var country:String,
    var organizerList:List<Organizers>,
    var pastEvent:List<PastEvents>,
    var upcomingEvent:List<UpcomingEvents>

)