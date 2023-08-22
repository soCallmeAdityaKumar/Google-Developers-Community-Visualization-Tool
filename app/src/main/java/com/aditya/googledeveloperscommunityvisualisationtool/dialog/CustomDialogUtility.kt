package com.aditya.googledeveloperscommunityvisualisationtool.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.media.Image
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.FragmentActivity
import com.airbnb.lottie.LottieAnimationView
import com.aditya.googledeveloperscommunityvisualisationtool.R
import org.jsoup.select.Evaluator.Id

object CustomDialogUtility {
    /**
     * It show a dialog
     * @param activity The activity where is call
     * @param message The message to pop up
     */
    @JvmStatic
    fun showDialog(fragment: FragmentActivity,layout:Int, heading:String?,message: String?) {
        val dialogFragment = CustomDialog(layout)
        val bundle = Bundle()
        bundle.putString("message", message)
        bundle.putString("heading",heading)
        dialogFragment.arguments = bundle
        dialogFragment.show(fragment.supportFragmentManager, "Image Dialog")
    }

    /**
     * It gives a dialog without Buttons
     * @param activity The activity where is call
     * @param message The message to pop up
     * @return The Dialog
     */
    @JvmStatic
    fun getDialog(fragment: FragmentActivity,drawable:Int, message: String?): Dialog {
        val builder=AlertDialog.Builder(fragment)
        @SuppressLint("InflateParams") val v = fragment.layoutInflater.inflate(R.layout.success_dialog, null)
//        val ok = v.findViewById<Button>(R.id.ok)
//        ok.visibility = View.GONE
        val textMessage = v.findViewById<TextView>(R.id.message1)
        v.findViewById<ImageView>(R.id.image).setBackgroundResource(drawable)
        textMessage.text = message
        textMessage.gravity = View.TEXT_ALIGNMENT_CENTER
        builder.setView(v)
        val dialog: Dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        v.findViewById<ImageView>(R.id.closeDialog).visibility=View.GONE
//        dialog.window?.setBackgroundDrawable(drawable)
        return dialog
    }
}