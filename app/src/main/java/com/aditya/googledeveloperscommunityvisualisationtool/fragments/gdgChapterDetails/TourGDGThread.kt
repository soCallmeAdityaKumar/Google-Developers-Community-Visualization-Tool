package com.aditya.googledeveloperscommunityvisualisationtool.fragments.gdgChapterDetails

import android.content.Context
import android.net.Uri
import android.util.EventLog
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.FragmentActivity
import com.aditya.googledeveloperscommunityvisualisationtool.create.utility.model.ActionController
import com.aditya.googledeveloperscommunityvisualisationtool.create.utility.model.ActionController.Companion.instance
import com.aditya.googledeveloperscommunityvisualisationtool.create.utility.model.balloon.Balloon
import com.aditya.googledeveloperscommunityvisualisationtool.create.utility.model.poi.POI
import com.aditya.googledeveloperscommunityvisualisationtool.create.utility.model.poi.POICamera
import com.aditya.googledeveloperscommunityvisualisationtool.create.utility.model.poi.POILocation
import com.aditya.googledeveloperscommunityvisualisationtool.fragments.home.GDGDetails
import com.aditya.googledeveloperscommunityvisualisationtool.fragments.home.Organizers
import com.aditya.googledeveloperscommunityvisualisationtool.fragments.home.PastEvents
import com.aditya.googledeveloperscommunityvisualisationtool.fragments.home.UpcomingEvents
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapterCompleteDetails.ChapterEntity
import com.google.gson.Gson
import kotlinx.coroutines.delay
import java.net.URI
import java.util.concurrent.atomic.AtomicBoolean

class TourGDGThread internal constructor(
    private val infoScraping: tourGDGDataclass,
    private val activity: FragmentActivity,
) : Runnable {
    private val running = AtomicBoolean(false)
    private var organizerList:String=""
    private var upcomingEventList:String=""
    private var pastEventList:String=""
    fun start() {
        val worker = Thread(this)
        worker.start()
    }

    fun stop() {
        running.set(false)
    }

    override fun run() {
        running.set(true)
        val duration = 15000
        val actionController = instance
        if (running.get()) {
            Log.w(TAG_DEBUG, "GOING")
            sendInformationLG(infoScraping,
                actionController)
            try {
                Log.w(TAG_DEBUG, "DURATION ACTION: $duration")
                Thread.sleep(duration.toLong())
            } catch (e: Exception) {
                Log.w(TAG_DEBUG, "ERROR: " + e.message)
            }
        }
        actionController!!.cleanFileKMLs(0)
        Log.w(TAG_DEBUG, "END")
//        activity.runOnUiThread {
//            buttTour.visibility = View.VISIBLE
//            buttStopTour.visibility = View.INVISIBLE
//        }
    }

    private fun sendInformationLG(gdg: tourGDGDataclass,actionController: ActionController?) {
        val poiLocation = POILocation(gdg.gdgName!!, gdg.lon, gdg.lat, 3000.0)
        val poiCamera = POICamera(10.0, 0.0, 3000.0, "absolute", 4)
        val poi = POI().setPoiLocation(poiLocation).setPoiCamera(poiCamera)
        val balloon = Balloon()
        var imageUri=""
        val description = gdg.about

        setValueFromPref(gdg.organizerList,gdg.pastEvent,gdg.upcomingEvent)

        Log.d("ballon",gdg.gdgName.toString())
        if(gdg.banner.isEmpty())imageUri="https://raw.githubusercontent.com/soCallmeAdityaKumar/Google-Developers-Community-Visualization-Tool/main/app/src/main/res/drawable/googledeveloper_placemark.png"
        else imageUri=gdg.banner
        balloon.setPoi(poi).setDescription(description)
            .setImageUri(Uri.parse(imageUri)).setImagePath(null).setVideoPath(null).setDuration(30).setCity(gdg.cityName).setCountry(gdg.country).setName(gdg.gdgName).setOrganizer(organizerList)
            .setUpcomingEvent(upcomingEventList).setPastEvent(pastEventList)
        actionController!!.TourGDG(poi, balloon)
    }

    private fun setValueFromPref(organiserList:List<Organizers>,pastEvent:List<PastEvents>,upcomingEvent:List<UpcomingEvents>) {

         organizerList = Gson().toJson(organiserList)
        Log.d("ballon",organizerList)

        pastEventList = Gson().toJson(pastEvent)
        Log.d("ballon",pastEventList)

         upcomingEventList = Gson().toJson(upcomingEvent)
        Log.d("ballon",upcomingEventList)


    }


    companion object {
        private const val TAG_DEBUG = "TourGDG"
    }
}