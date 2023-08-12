package com.aditya.googledeveloperscommunityvisualisationtool.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.airbnb.lottie.LottieAnimationView
import com.aditya.googledeveloperscommunityvisualisationtool.R
import java.util.Objects

class CustomDialog() : DialogFragment() {
    //private final static String TAG_DEBUG = "DEBUGG_TOAST";
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        @SuppressLint("InflateParams") val v = inflater.inflate(R.layout.dialog_fragment, null)
        v.background.alpha = 220
        val textMessage = v.findViewById<TextView>(R.id.message)
        val bundle = arguments
        val text = Objects.requireNonNull(bundle)?.getString("TEXT", "")
        textMessage.text = text
        val ok = v.findViewById<Button>(R.id.ok)
        ok.setOnClickListener { view: View? -> dismiss() }
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(v)
        return builder.create()
    }
}