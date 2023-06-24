package com.example.googledeveloperscommunityvisualisationtool.create.utility.model.balloon

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.os.Parcel
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Log
import com.example.googledeveloperscommunityvisualisationtool.create.utility.IJsonPacker
import com.example.googledeveloperscommunityvisualisationtool.create.utility.model.Action
import com.example.googledeveloperscommunityvisualisationtool.create.utility.model.ActionIdentifier
import com.example.googledeveloperscommunityvisualisationtool.create.utility.model.poi.POI
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.nio.charset.StandardCharsets

class Balloon : Action, IJsonPacker<Any?>, Parcelable {
    var poi: POI? = null
        private set
    var description: String? = null
        private set
    var imageUri: Uri? = null
        private set
    var imagePath: String? = null
        private set
    var videoPath: String? = null
        private set
    var duration = 0
        private set

    /**
     * Empty Constructor
     */
    constructor() : super(ActionIdentifier.BALLOON_ACTIVITY.id) {}
    constructor(
        id: Long,
        poi: POI?,
        description: String?,
        imageUri: Uri?,
        imagePath: String?,
        videoPath: String?,
        duration: Int
    ) : super(id, ActionIdentifier.BALLOON_ACTIVITY.id) {
        this.poi = poi
        this.description = description
        this.imageUri = imageUri
        this.imagePath = imagePath
        this.videoPath = videoPath
        this.duration = duration
    }

    constructor(`in`: Parcel) : super(`in`.readLong(), ActionIdentifier.BALLOON_ACTIVITY.id) {
        poi = `in`.readParcelable(POI::class.java.classLoader)
        description = `in`.readString()
        imageUri = `in`.readParcelable(Uri::class.java.classLoader)
        imagePath = `in`.readString()
        videoPath = `in`.readString()
        duration = `in`.readInt()
    }

    constructor(balloon: Balloon) : super(balloon.id, ActionIdentifier.BALLOON_ACTIVITY.id) {
        poi = balloon.poi
        description = balloon.description
        imageUri = balloon.imageUri
        imagePath = balloon.imagePath
        videoPath = balloon.videoPath
        duration = balloon.duration
    }

    fun setPoi(poi: POI?): Balloon {
        this.poi = poi
        return this
    }

    fun setDescription(description: String?): Balloon {
        this.description = description
        return this
    }

    fun setImageUri(imageUri: Uri?): Balloon {
        this.imageUri = imageUri
        return this
    }

    fun setImagePath(imagePath: String?): Balloon {
        this.imagePath = imagePath
        return this
    }

    fun setVideoPath(videoPath: String?): Balloon {
        this.videoPath = videoPath
        return this
    }

    fun setDuration(duration: Int): Balloon {
        this.duration = duration
        return this
    }

    @Throws(JSONException::class)
    override fun pack(): JSONObject? {
        val obj = JSONObject()
        obj.put("balloon_id", id)
        obj.put("type", type)
        if (poi != null) obj.put("place_mark_poi", poi!!.pack())
        obj.put("description", description)
        obj.put("image_uri", if (imageUri != null) imageUri.toString() else "")
        obj.put("image_path", if (imagePath != null) imagePath else "")
        var encodedImage: String? = ""
        if (imagePath != null) {
            encodedImage = encodeFileToBase64Binary()
        }
        if (encodedImage == null) encodedImage = ""
        obj.put("encodedImage", encodedImage)
        obj.put("video_path", if (videoPath != null) videoPath else "")
        obj.put("duration", duration)
        return obj
    }

    private fun encodeFileToBase64Binary(): String? {
        val imageFile = File(imagePath)
        var encodedFile: String? = null
        try {
            val fis = FileInputStream(imageFile)
            val bm = BitmapFactory.decodeStream(fis)
            val out = ByteArrayOutputStream()
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out)
            val bytes = out.toByteArray()
//            encodedFile = String(Base64.encodeBase64(bytes), StandardCharsets.UTF_8)
        } catch (e: IOException) {
            Log.w(TAG_DEBUG, "ERROR: " + e.message)
        }
        return encodedFile
    }

    @Throws(JSONException::class)
    override fun unpack(obj: JSONObject?): Balloon {
        id = obj!!.getLong("balloon_id")
        type = obj.getInt("type")
        val newPoi = POI()
        poi = try {
            newPoi.unpack(obj.getJSONObject("place_mark_poi"))
        } catch (JSONException: JSONException) {
            null
        }
        description = obj.getString("description")
        val uri = obj.getString("image_uri")
        imageUri = if (uri != "") Uri.parse(obj.getString("image_uri")) else null
        imagePath = obj.getString("image_path")
        videoPath = obj.getString("video_path")
        duration = obj.getInt("duration")
        return this
    }

    @Throws(JSONException::class)
    fun unpackBalloon(obj: JSONObject, context: Context): Balloon {
        id = obj.getLong("balloon_id")
        type = obj.getInt("type")
        val newPoi = POI()
        poi = try {
            newPoi.unpack(obj.getJSONObject("place_mark_poi"))
        } catch (JSONException: JSONException) {
            null
        }
        description = obj.getString("description")
        val uri = obj.getString("image_uri")
        imageUri = if (uri != "") Uri.parse(obj.getString("image_uri")) else null
        imagePath = obj.getString("image_path")
        val encodedImage = obj.getString("encodedImage")
        if (encodedImage != "") {
            Log.w(TAG_DEBUG, "Encoded Image: $encodedImage")
            try {
//                val imageByte = Base64.decodeBase64(encodedImage)
//                val decodedByte = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.size)
                val route =
                    imagePath?.split("/".toRegex())!!.dropLastWhile { it.isEmpty() }.toTypedArray()
                val fileName = route[route.size - 1]
                val path =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        .toString()
                val fOut: OutputStream
                var file = File(path, fileName)
                Log.w(TAG_DEBUG, "PATH VERIFY: " + file.absolutePath)
                if (!file.exists()) {
                    Log.w(TAG_DEBUG, "FILE DOESN'T EXIST")
                    // the File to save , append increasing numeric counter to prevent files from getting overwritten.
                    file = File(path, fileName)
                    fOut = FileOutputStream(file)
//                    decodedByte.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                    fOut.flush()
                    fOut.close()
                    val url = MediaStore.Images.Media.insertImage(
                        context.contentResolver,
                        file.absolutePath,
                        file.name,
                        file.name
                    )
                    imageUri = Uri.parse(url)
                    imagePath = file.absolutePath
                } else {
                    MediaScannerConnection.scanFile(
                        context, arrayOf(file.absolutePath), null
                    ) { pathImage: String?, uriImage: Uri? ->
                        imageUri = uriImage
                        Log.w(TAG_DEBUG, "URI: $imageUri")
                    }
                    imagePath = file.absolutePath
                }
            } catch (e: Exception) {
                Log.w(TAG_DEBUG, "ERROR: " + e.message)
            }
        }
        videoPath = obj.getString("video_path")
        duration = obj.getInt("duration")
        return this
    }

    override fun toString(): String {
        return "Location Name: " + poi!!.poiLocation?.name + " Image Uri: " + imageUri.toString() + " Video URL: " + videoPath
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeParcelable(poi, flags)
        parcel.writeString(description)
        parcel.writeParcelable(imageUri, flags)
        parcel.writeString(imagePath)
        parcel.writeString(videoPath)
        parcel.writeInt(duration)
    }

    companion object CREATOR : Parcelable.Creator<Balloon> {
        private const val TAG_DEBUG = "Balloon"

        override fun createFromParcel(parcel: Parcel): Balloon {
            return Balloon(parcel)
        }

        override fun newArray(size: Int): Array<Balloon?> {
            return arrayOfNulls(size)
        }
    }
}