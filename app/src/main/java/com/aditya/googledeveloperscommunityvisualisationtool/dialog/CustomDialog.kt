package com.aditya.googledeveloperscommunityvisualisationtool.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.airbnb.lottie.LottieAnimationView
import com.aditya.googledeveloperscommunityvisualisationtool.R
import com.android.car.ui.recyclerview.CarUiListItemAdapterAdapterV1.ViewHolderWrapper
import org.jsoup.select.Evaluator.IsNthOfType
import java.util.Objects

class CustomDialog(val image:Int) : DialogFragment() {
    //private final static String TAG_DEBUG = "DEBUGG_TOAST";
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        @SuppressLint("InflateParams") val v = inflater.inflate(R.layout.success_dialog, null)
        val secondaryMessage= v.findViewById<TextView>(R.id.message2)
        secondaryMessage.visibility=View.VISIBLE
        val bundle = arguments
        val text1 = Objects.requireNonNull(bundle)?.getString("message", "")
        secondaryMessage.text = text1
        val heading=v.findViewById<TextView>(R.id.message1)
        val text2=Objects.requireNonNull(bundle)?.getString("heading","")
        heading.text=text2
//        val ok = v.findViewById<Button>(R.id.ok)
//        ok.setOnClickListener { view: View? -> dismiss() }
//        val builder = AlertDialog.Builder(requireActivity())
//        builder.setView(v)
        val dialog=Dialog(requireContext())
        dialog.setContentView(v)
        dialog.findViewById<ImageView>(R.id.image).setImageDrawable(resources.getDrawable(image))
        v.findViewById<ImageView>(R.id.closeDialog).setOnClickListener {
            dialog.cancel()
        }
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }
}