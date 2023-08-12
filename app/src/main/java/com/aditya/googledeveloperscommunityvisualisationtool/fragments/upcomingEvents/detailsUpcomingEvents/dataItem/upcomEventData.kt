package com.aditya.googledeveloperscommunityvisualisationtool.fragments.upcomingEvents.detailsUpcomingEvents.dataItem

import com.aditya.googledeveloperscommunityvisualisationtool.dataFetching.oldData.oldEvents.oldEventsDataClass.Organizer
import com.aditya.googledeveloperscommunityvisualisationtool.fragments.home.Organizers


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
