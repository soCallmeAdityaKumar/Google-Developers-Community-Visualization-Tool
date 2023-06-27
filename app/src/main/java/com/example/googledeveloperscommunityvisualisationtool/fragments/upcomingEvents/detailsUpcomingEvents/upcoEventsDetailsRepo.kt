package com.example.googledeveloperscommunityvisualisationtool.fragments.upcomingEvents.detailsUpcomingEvents

import android.util.Log
import com.example.googledeveloperscommunityvisualisationtool.fragments.home.Organizers
import com.example.googledeveloperscommunityvisualisationtool.fragments.upcomingEvents.detailsUpcomingEvents.dataItem.upcomEventData
import org.jsoup.Jsoup

class upcoEventsDetailsRepo{
private lateinit var details:upcomEventData
    fun getResponse(baseurl:String){
        val doc=Jsoup.connect(baseurl).get()
        //event title
        val title=doc.getElementsByClass("font_banner2").text()
        val address=doc.getElementsByClass("event-header-address").text()
        val gdgname=doc.getElementsByClass("event-chapter-title").text()
        val dateAndTime=doc.getElementsByClass("event-date-time").text()
        val rsvp=doc.getElementsByClass("event-attendees").text()
        val desc=doc.getElementsByClass("event-description general-body").text()
        val duration=doc.getElementsByClass("title-span").text()
        //mentors
        val mentors= mutableListOf<Organizers>()
        val people=doc.getElementsByClass("people-card general-card")
        for(i in 0 until people.size){
            val name=people[i].getElementsByClass("people-card--name").text()
            val company=people[i].getElementsByClass("people-card--company").text()
            val title=people[i].getElementsByClass("people-card--title").text()
            val image=people[i].getElementsByTag("img").attr("src")
            mentors.add(Organizers(name, company, title, image))
            Log.d("mentor","$name,$company,$title,$image")
        }
        details= upcomEventData(title,address,gdgname,dateAndTime,rsvp,desc,duration,mentors)
    }
    fun returndetails():upcomEventData{
        return details
    }
}