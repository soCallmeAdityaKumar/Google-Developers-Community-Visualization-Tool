package com.example.googledeveloperscommunityvisualisationtool.Fragments.Home

data class GDGDetails(
    val gdgName:String,
    val membersNumber:String,
    val about:String,
    val pastEventsList:ArrayList<PastEvents>,
    val upcomingEventsList:ArrayList<UpcomingEvents>,
    val orgnaizersList:ArrayList<Organizers>
)
data class PastEvents(
    val pastEventstitle:String,
    val pastEventsdate:String,
    val pastEventstype:String,
    val pastEventslink:String
)
data class UpcomingEvents(
    val upcomingEventstitle:String,
    val upcomingEventsdate:String,
    val upcomingEventslink:String,
    val upcomingEventsdescription:String
)

data class Organizers(
    val organizername:String,
    val organizercompany:String,
    val organizerTitle:String,
    val organizerimage:String
)
