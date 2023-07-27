package com.example.googledeveloperscommunityvisualisationtool.fragments.home

data class GDGDetails(
    val gdgName:String,
    val membersNumber:String,
    val about:String,
    val pastEventsList:List<PastEvents>,
    val upcomingEventsList:List<UpcomingEvents>,
    val orgnaizersList:List<Organizers>,
    val instagramLink:String,
    val emailLink:String,
    val twitterLink:String,
    val linkedIn:String,
    val facebookLink:String
)




