package com.example.googledeveloperscommunityvisualisationtool.fragments.upcomingEvents.detailsUpcomingEvents.dataItem

import com.example.googledeveloperscommunityvisualisationtool.dataFetching.oldData.oldEvents.oldEventsDataClass.Organizer
import com.example.googledeveloperscommunityvisualisationtool.fragments.home.Organizers


data class upcomEventData(
    val title:String,
    val address:String,
    val gdgName:String,
    val dateAndTime:String,
    val rsvp:String,
    val desc:String,
    val duration:String,
    val mentors:Set<Organizers>
    )
