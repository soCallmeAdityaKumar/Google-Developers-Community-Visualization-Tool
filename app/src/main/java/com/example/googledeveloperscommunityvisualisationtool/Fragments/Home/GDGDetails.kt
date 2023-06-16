package com.example.googledeveloperscommunityvisualisationtool.Fragments.Home

data class GDGDetails(
    val gdgName:String,
    val membersNumber:String,
    val about:String,
    val pastEventsList:List<PastEvents>,
    val upcomingEventsList:List<UpcomingEvents>,
    val orgnaizersList:List<Organizers>
)




