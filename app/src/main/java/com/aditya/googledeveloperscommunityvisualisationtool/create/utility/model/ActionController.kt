package com.aditya.googledeveloperscommunityvisualisationtool.create.utility.model

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.aditya.googledeveloperscommunityvisualisationtool.connection.LGCommand
import com.aditya.googledeveloperscommunityvisualisationtool.connection.LGConnectionManager
import com.aditya.googledeveloperscommunityvisualisationtool.connection.LGConnectionSendFile
import com.aditya.googledeveloperscommunityvisualisationtool.create.utility.model.ActionBuildCommandUtility.buildCleanKMLs
import com.aditya.googledeveloperscommunityvisualisationtool.create.utility.model.ActionBuildCommandUtility.buildCleanQuery
import com.aditya.googledeveloperscommunityvisualisationtool.create.utility.model.ActionBuildCommandUtility.buildCommandBalloonTest
import com.aditya.googledeveloperscommunityvisualisationtool.create.utility.model.ActionBuildCommandUtility.buildCommandBalloonWithLogos
import com.aditya.googledeveloperscommunityvisualisationtool.create.utility.model.ActionBuildCommandUtility.buildCommandCleanSlaves
import com.aditya.googledeveloperscommunityvisualisationtool.create.utility.model.ActionBuildCommandUtility.buildCommandCreateResourcesFolder
import com.aditya.googledeveloperscommunityvisualisationtool.create.utility.model.ActionBuildCommandUtility.buildCommandExitTour
import com.aditya.googledeveloperscommunityvisualisationtool.create.utility.model.ActionBuildCommandUtility.buildCommandOrbit
import com.aditya.googledeveloperscommunityvisualisationtool.create.utility.model.ActionBuildCommandUtility.buildCommandPOITest
import com.aditya.googledeveloperscommunityvisualisationtool.create.utility.model.ActionBuildCommandUtility.buildCommandSendShape
import com.aditya.googledeveloperscommunityvisualisationtool.create.utility.model.ActionBuildCommandUtility.buildCommandStartOrbit
import com.aditya.googledeveloperscommunityvisualisationtool.create.utility.model.ActionBuildCommandUtility.buildCommandStartTour
import com.aditya.googledeveloperscommunityvisualisationtool.create.utility.model.ActionBuildCommandUtility.buildCommandTour
import com.aditya.googledeveloperscommunityvisualisationtool.create.utility.model.ActionBuildCommandUtility.buildCommandWriteOrbit
import com.aditya.googledeveloperscommunityvisualisationtool.create.utility.model.ActionBuildCommandUtility.buildCommandwriteStartTourFile
import com.aditya.googledeveloperscommunityvisualisationtool.create.utility.model.ActionBuildCommandUtility.buildWriteBalloonFile
import com.aditya.googledeveloperscommunityvisualisationtool.create.utility.model.ActionBuildCommandUtility.buildWriteShapeFile
import com.aditya.googledeveloperscommunityvisualisationtool.create.utility.model.Shape.Shape
import com.aditya.googledeveloperscommunityvisualisationtool.create.utility.model.balloon.Balloon
import com.aditya.googledeveloperscommunityvisualisationtool.create.utility.model.poi.POI
import java.io.File
import java.io.FileOutputStream

class ActionController
/**
 * Enforce private constructor
 */
private constructor() {
    private val handler = Handler(Looper.getMainLooper())
    private val handler2 = Handler(Looper.getMainLooper())

    /**
     * Move the screen to the poi
     *
     * @param poi      The POI that is going to move
     * @param listener The listener of lgcommand
     */
    fun moveToPOI(poi: POI, listener: LGCommand.Listener?) {
        cleanFileKMLs(0)
        sendPoiToLG(poi, listener)
    }

    /**
     * Create the lGCommand to send to the liquid galaxy
     *
     * @param listener The LGCommand listener
     */
    private fun sendPoiToLG(poi: POI, listener: LGCommand.Listener?) {
        val lgCommand = LGCommand(
            buildCommandPOITest(poi),
            LGCommand.CRITICAL_MESSAGE,
            object:LGCommand.Listener {
                override fun onResponse(response: String?) {
                    listener?.onResponse(response)
                }
            })
        val lgConnectionManager = LGConnectionManager.getInstance()
        lgConnectionManager!!.startConnection()
        lgConnectionManager.addCommandToLG(lgCommand)
    }

    /**
     * First Clean the KML and then do the orbit
     *
     * @param poi      POI
     * @param listener Listener
     */
    @Synchronized
    fun cleanOrbit(poi: POI?) {
        cleanFileKMLs(0)
        orbit(poi,null)
    }

    /**
     * Do the orbit
     *
     * @param poi      POI
     * @param listener Listener
     */
    fun orbit(poi: POI?,listener: LGCommand.Listener?) {
        val lgCommandOrbit = LGCommand(
            buildCommandOrbit(poi),
            LGCommand.CRITICAL_MESSAGE,
            object :LGCommand.Listener {
                override fun onResponse(response: String?) {
                    listener?.onResponse(response)
                }
            })
        val lgConnectionManager = LGConnectionManager.getInstance()
        lgConnectionManager!!.startConnection()
        lgConnectionManager.addCommandToLG(lgCommandOrbit)
        val lgCommandWriteOrbit = LGCommand(
            buildCommandWriteOrbit(),
            LGCommand.CRITICAL_MESSAGE,
            object :LGCommand.Listener {
                override fun onResponse(response: String?) {
                    listener?.onResponse(response)
                }
            })
        lgConnectionManager.addCommandToLG(lgCommandWriteOrbit)
        val lgCommandStartOrbit = LGCommand(
            buildCommandStartOrbit(),
            LGCommand.CRITICAL_MESSAGE,
            object :LGCommand.Listener {
                override fun onResponse(response: String?) {
                    listener?.onResponse(response)
                }
            })
        handler.postDelayed({ lgConnectionManager.addCommandToLG(lgCommandStartOrbit) }, 500)
        cleanFileKMLs(46000)
    }

    /**
     * @param balloon  Balloon with the information to build command
     * @param listener listener
     */
    fun sendBalloon(balloon: Balloon, listener: LGCommand.Listener?) {
        cleanFileKMLs(0)
        val imageUri = balloon.imageUri
        if (imageUri != null) {
            createResourcesFolder()
            val imagePath = balloon.imagePath
            val lgConnectionSendFile = LGConnectionSendFile.getInstance()
            lgConnectionSendFile!!.addPath(imagePath)
            lgConnectionSendFile.startConnection()
        }
        handler.postDelayed({
            val lgCommand = LGCommand(
                buildCommandBalloonTest(balloon),
                LGCommand.CRITICAL_MESSAGE,
                object :LGCommand.Listener {
                    override fun onResponse(response: String?) {
                        listener?.onResponse(response)
                    }
                })
            val lgConnectionManager = LGConnectionManager.getInstance()
            lgConnectionManager!!.startConnection()
            lgConnectionManager.addCommandToLG(lgCommand)
            handler.postDelayed({ writeFileBalloonFile() }, 500)
        }, 500)
    }

    /**
     * @param balloon  Balloon with the information to build command
     * @param listener listener
     */
    fun sendBalloonTestStoryBoard(balloon: Balloon?, listener: LGCommand.Listener?) {
        cleanFileKMLs(0)
        val lgCommand = LGCommand(
            buildCommandBalloonTest(balloon!!),
            LGCommand.CRITICAL_MESSAGE,
            object :LGCommand.Listener {
                override fun onResponse(response: String?) {
                    listener?.onResponse(response)
                }
            })
        val lgConnectionManager = LGConnectionManager.getInstance()
        lgConnectionManager!!.startConnection()
        lgConnectionManager.addCommandToLG(lgCommand)
        handler.postDelayed({ writeFileBalloonFile() }, 500)
    }

    /**
     * Send the image of the balloon
     *
     * @param balloon Balloon
     */
    fun sendImageTestStoryboard(balloon: Balloon) {
        val imageUri = balloon.imageUri
        if (imageUri != null) {
            val imagePath = balloon.imagePath
            Log.w(TAG_DEBUG, "Image Path: $imagePath")
            val lgConnectionSendFile = LGConnectionSendFile.getInstance()
            lgConnectionSendFile!!.addPath(imagePath)
            lgConnectionSendFile.startConnection()
        }
    }

    /**
     * Paint a balloon with the logos
     */
    fun sendBalloonWithLogos(activity: FragmentActivity) {
        createResourcesFolder()
        val imagePath = getLogosFile(activity)
        val lgConnectionSendFile = LGConnectionSendFile.getInstance()
        lgConnectionSendFile!!.addPath(imagePath)
        lgConnectionSendFile.startConnection()
        cleanFileKMLs(0)
        handler.postDelayed({
            val lgCommand = LGCommand(
                buildCommandBalloonWithLogos(),
                LGCommand.CRITICAL_MESSAGE, object :LGCommand.Listener {
                    override fun onResponse(response: String?) {

                    }
                })
            val lgConnectionManager = LGConnectionManager.getInstance()
            lgConnectionManager!!.startConnection()
            lgConnectionManager.addCommandToLG(lgCommand)
        }, 2000)
    }

    private fun getLogosFile(activity: FragmentActivity): String {
        val file = File(activity.cacheDir.toString() + "/logos.png")
        if (!file.exists()) {
            try {
                val `is` = activity.assets.open("logos.png")
                val size = `is`.available()
                Log.w(TAG_DEBUG, "SIZE: $size")
                val buffer = ByteArray(size)
                `is`.read(buffer)
                `is`.close()
                val fos = FileOutputStream(file)
                fos.write(buffer)
                fos.close()
                return file.path
            } catch (e: Exception) {
                Log.w(TAG_DEBUG, "ERROR: " + e.message)
            }
        }
        return file.path
    }

    /**
     * Create the Resource folder
     */
    fun createResourcesFolder() {
        val lgCommand = LGCommand(
            buildCommandCreateResourcesFolder(),
            LGCommand.CRITICAL_MESSAGE,
            object :LGCommand.Listener {
                override fun onResponse(response: String?) {

                }
            })
        val lgConnectionManager = LGConnectionManager.getInstance()
        lgConnectionManager!!.startConnection()
        lgConnectionManager.addCommandToLG(lgCommand)
    }

    /**
     * Write the shape.kml in the Liquid Galaxy
     */
    private fun writeFileShapeFile() {
        val lgCommand = LGCommand(
            buildWriteShapeFile(),
            LGCommand.CRITICAL_MESSAGE,object :LGCommand.Listener {
                override fun onResponse(response: String?) {

                }
            })
        val lgConnectionManager = LGConnectionManager.getInstance()
        lgConnectionManager!!.startConnection()
        lgConnectionManager.addCommandToLG(lgCommand)
    }

    /**
     * Send the command to liquid galaxy
     *
     * @param shape    Shape with the information to build the command
     * @param listener listener
     */
    fun sendShape(shape: Shape?, listener: LGCommand.Listener?) {
        cleanFileKMLs(0)
        val lgCommand = LGCommand(
            buildCommandSendShape(shape!!),
            LGCommand.CRITICAL_MESSAGE,
            object :LGCommand.Listener {
                override fun onResponse(response: String?) {
                    listener?.onResponse(response)
                }
            })
        val lgConnectionManager = LGConnectionManager.getInstance()
        lgConnectionManager!!.startConnection()
        lgConnectionManager.addCommandToLG(lgCommand)
        handler.postDelayed({ writeFileShapeFile() }, 500)
    }

    /**
     * It cleans the kmls.txt file
     */
    fun cleanFileKMLs(duration: Int) {
        handler.postDelayed({
            val lgCommand = LGCommand(
                buildCleanKMLs(),
                LGCommand.CRITICAL_MESSAGE, object :LGCommand.Listener {
                    override fun onResponse(response: String?) {

                    }
                })
            val lgConnectionManager = LGConnectionManager.getInstance()
            lgConnectionManager!!.startConnection()
            lgConnectionManager.addCommandToLG(lgCommand)
        }, duration.toLong())
    }

    /**
     * It cleans the kmls.txt file
     */
    fun cleanQuery(duration: Int) {
        handler.postDelayed({
            val lgCommand = LGCommand(
                buildCleanQuery(),
                LGCommand.CRITICAL_MESSAGE, object :LGCommand.Listener {
                    override fun onResponse(response: String?) {

                    }
                })
            val lgConnectionManager = LGConnectionManager.getInstance()
            lgConnectionManager!!.startConnection()
            lgConnectionManager.addCommandToLG(lgCommand)
        }, duration.toLong())
    }

    /**
     * Send both command to the Liquid Galaxy
     *
     * @param poi     Poi with the location information
     * @param balloon Balloon with the information to paint the balloon
     */
    fun TourGDG(poi: POI, balloon: Balloon) {
        cleanFileKMLs(0)
        sendBalloonTourGDG(balloon, null)
        sendPoiToLG(poi, null)
    }

    /**
     * Send a balloon in the case of the tour
     *
     * @param balloon  Balloon with the information to build command
     * @param listener listener
     */
    private fun sendBalloonTourGDG(balloon: Balloon, listener: LGCommand.Listener?) {
        val lgCommand = LGCommand(
            buildCommandBalloonTest(balloon),
            LGCommand.CRITICAL_MESSAGE,
            object :LGCommand.Listener {
                override fun onResponse(response: String?) {
                    listener?.onResponse(response)
                }
            })
        val lgConnectionManager = LGConnectionManager.getInstance()
        lgConnectionManager!!.startConnection()
        lgConnectionManager.addCommandToLG(lgCommand)
        handler.postDelayed({ writeFileBalloonFile() }, 1000)
    }

    /**
     * Write the file of the balloon
     */
    private fun writeFileBalloonFile() {
        val lgCommand = LGCommand(
            buildWriteBalloonFile(),
            LGCommand.CRITICAL_MESSAGE, object:LGCommand.Listener {
                override fun onResponse(response: String?) {

                }
            })
        val lgConnectionManager = LGConnectionManager.getInstance()
        lgConnectionManager!!.startConnection()
        lgConnectionManager.addCommandToLG(lgCommand)
    }

    /**
     * Send the tour kml
     * @param actions Storyboard's actions
     * @param listener Listener
     */
    fun sendTour(actions: List<Action?>?, listener: LGCommand.Listener?) {
        cleanFileKMLs(0)
        handler.postDelayed({
            val lgCommand = LGCommand(
                buildCommandTour(actions as List<Action>),
                LGCommand.CRITICAL_MESSAGE,
                object :LGCommand.Listener {
                    override fun onResponse(response: String?) {
                        listener?.onResponse(response)
                    }
                })
            val lgConnectionManager = LGConnectionManager.getInstance()
            lgConnectionManager!!.startConnection()
            lgConnectionManager.addCommandToLG(lgCommand)
            val lgCommandWriteTour = LGCommand(
                buildCommandwriteStartTourFile(),
                LGCommand.CRITICAL_MESSAGE,
                object :LGCommand.Listener {
                    override fun onResponse(response: String?) {
                        listener?.onResponse(response)
                    }
                })
            lgConnectionManager.addCommandToLG(lgCommandWriteTour)
            val lgCommandStartTour = LGCommand(
                buildCommandStartTour(),
                LGCommand.CRITICAL_MESSAGE, object :LGCommand.Listener {
                    override fun onResponse(response: String?) {

                    }
                })
            handler2.postDelayed({ lgConnectionManager.addCommandToLG(lgCommandStartTour) }, 1500)
        }, 1000)
    }

    /**
     * Exit Tour
     */
    fun exitTour() {
        cleanFileKMLs(0)
        val lgCommand = LGCommand(
            buildCommandExitTour(),
            LGCommand.CRITICAL_MESSAGE, object :LGCommand.Listener {
                override fun onResponse(response: String?) {

                }
            })
        val lgConnectionManager = LGConnectionManager.getInstance()
        lgConnectionManager!!.startConnection()
        lgConnectionManager.addCommandToLG(lgCommand)
        val lgCommandCleanSlaves = LGCommand(
            buildCommandCleanSlaves(),
            LGCommand.CRITICAL_MESSAGE,  object :LGCommand.Listener {
                override fun onResponse(response: String?) {

                }
            })
        lgConnectionManager.addCommandToLG(lgCommandCleanSlaves)
    }

    companion object {
        private const val TAG_DEBUG = "ActionController"

        @JvmStatic
        @get:Synchronized
        var instance: ActionController? = null
            get() {
                if (field == null) field = ActionController()
                return field
            }
            private set
    }
}