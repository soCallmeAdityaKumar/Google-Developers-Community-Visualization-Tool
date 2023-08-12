package com.aditya.googledeveloperscommunityvisualisationtool.fragments.gdgChapterDetails

import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.aditya.googledeveloperscommunityvisualisationtool.create.utility.model.ActionController
import com.aditya.googledeveloperscommunityvisualisationtool.create.utility.model.balloon.Balloon
import com.aditya.googledeveloperscommunityvisualisationtool.create.utility.model.poi.POI
import com.aditya.googledeveloperscommunityvisualisationtool.create.utility.model.poi.POICamera
import com.aditya.googledeveloperscommunityvisualisationtool.create.utility.model.poi.POILocation
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapterCompleteDetails.ChapterEntity
import java.util.concurrent.atomic.AtomicBoolean

class OrbitGDGThread internal constructor(
    private val infoScraping: ChapterEntity,
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
        Log.w(TAG_DEBUG, "Inside the Run")
        val actionController = ActionController.instance
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
    }

    private fun sendInformationLG(gdg: ChapterEntity, actionController: ActionController?) {
        val poiLocation = POILocation(gdg.gdgName!!, gdg.longitude, gdg.latitude, 3000.0)
        val poiCamera = POICamera(10.0, 0.0, 3000.0, "absolute", 4)
        val poi = POI().setPoiLocation(poiLocation).setPoiCamera(poiCamera)
        actionController!!.cleanOrbit(poi)

    }

    companion object {
        private const val TAG_DEBUG = "OrbitGDG"
    }
}