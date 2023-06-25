package com.example.googledeveloperscommunityvisualisationtool.Fragments.GDGChapterDetails

import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.FragmentActivity
import com.example.googledeveloperscommunityvisualisationtool.create.utility.model.ActionController
import com.example.googledeveloperscommunityvisualisationtool.create.utility.model.ActionController.Companion.instance
import com.example.googledeveloperscommunityvisualisationtool.create.utility.model.balloon.Balloon
import com.example.googledeveloperscommunityvisualisationtool.create.utility.model.poi.POI
import com.example.googledeveloperscommunityvisualisationtool.create.utility.model.poi.POICamera
import com.example.googledeveloperscommunityvisualisationtool.create.utility.model.poi.POILocation
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapterCompleteDetails.ChapterEntity
import java.util.concurrent.atomic.AtomicBoolean

class TourGDGThread internal constructor(
    private val infoScraping: ChapterEntity,
    private val activity: FragmentActivity,
    private val buttTour: Button,
    private val buttStopTour: Button
) : Runnable {
    private val running = AtomicBoolean(false)
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
            sendInformationLG(infoScraping as ChapterEntity, actionController)
            try {
                Log.w(TAG_DEBUG, "DURATION ACTION: $duration")
                Thread.sleep(duration.toLong())
            } catch (e: Exception) {
                Log.w(TAG_DEBUG, "ERROR: " + e.message)
            }
        }
        actionController!!.cleanFileKMLs(0)
        Log.w(TAG_DEBUG, "END")
        activity.runOnUiThread {
            buttTour.visibility = View.VISIBLE
            buttStopTour.visibility = View.INVISIBLE
        }
    }

    private fun sendInformationLG(gdg: ChapterEntity, actionController: ActionController?) {
        val poiLocation = POILocation(gdg.gdgName!!, gdg.longitude, gdg.latitude, 3000.0)
        val poiCamera = POICamera(10.0, 0.0, 3000.0, "absolute", 4)
        val poi = POI().setPoiLocation(poiLocation).setPoiCamera(poiCamera)
        val balloon = Balloon()
        val description = gdg.city + ", " + gdg.country
        balloon.setPoi(poi).setDescription(description)
            .setImageUri(null).setImagePath(null).setVideoPath(null).setDuration(15)
        actionController!!.TourGDG(poi, balloon)
    }

    companion object {
        private const val TAG_DEBUG = "TourGDG"
    }
}