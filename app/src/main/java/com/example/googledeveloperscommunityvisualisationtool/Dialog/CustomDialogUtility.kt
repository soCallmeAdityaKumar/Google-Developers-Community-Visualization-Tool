package com.example.googledeveloperscommunityvisualisationtool.Dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.example.googledeveloperscommunityvisualisationtool.R

object CustomDialogUtility {
    /**
     * It show a dialog
     * @param activity The activity where is call
     * @param message The message to pop up
     */
    @JvmStatic
    fun showDialog(fragment: FragmentActivity, message: String?) {
        val dialogFragment = CustomDialog()
        val bundle = Bundle()
        bundle.putString("TEXT", message)
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
    fun getDialog(fragment: FragmentActivity, message: String?): Dialog {
        val builder = AlertDialog.Builder(fragment)
        @SuppressLint("InflateParams") val v =
            fragment.layoutInflater.inflate(R.layout.dialog_fragment, null)
        v.background.alpha = 220
        val ok = v.findViewById<Button>(R.id.ok)
        ok.visibility = View.GONE
        val textMessage = v.findViewById<TextView>(R.id.message)
        textMessage.text = message
        textMessage.textSize = 23f
        textMessage.gravity = View.TEXT_ALIGNMENT_CENTER
        builder.setView(v)
        val dialog: Dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }
}