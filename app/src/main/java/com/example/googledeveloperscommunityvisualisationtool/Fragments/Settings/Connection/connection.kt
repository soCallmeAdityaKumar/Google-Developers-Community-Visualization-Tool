package com.example.googledeveloperscommunityvisualisationtool.Fragments.Settings.Connection

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import com.example.googledeveloperscommunityvisualisationtool.Fragments.Settings.Settings
import com.example.googledeveloperscommunityvisualisationtool.R
import com.example.googledeveloperscommunityvisualisationtool.databinding.FragmentConnectionBinding
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetView


class connection : Fragment() {
    private  lateinit var binding:FragmentConnectionBinding
    private lateinit var sharedPref:SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

         binding=FragmentConnectionBinding.inflate(layoutInflater, container, false)
        val view=binding.root

         sharedPref= activity?.getSharedPreferences("didShowPrompt", Context.MODE_PRIVATE)!!
        val prefEdit=sharedPref?.edit()

        showIpAddresstap()



        return view
    }

    private fun showIpAddresstap() {
        if(!sharedPref.getBoolean("didShowPrompt",false)){
            TapTargetView.showFor(this.requireActivity(),
                TapTarget.forView(
                    binding.IpAddressEditText,
                    "Enter the Main Slave Ip Address ",
                    "")
                    .outerCircleColor(androidx.appcompat.R.color.material_deep_teal_200)
                    .outerCircleAlpha(0.96f)
                    .targetCircleColor(R.color.white)
                    .titleTextSize(20)
                    .titleTextColor(R.color.white)
                    .descriptionTextSize(10)
                    .descriptionTextColor(R.color.black)
                    .textColor(R.color.black)
                    .textTypeface(Typeface.SANS_SERIF)
                    .dimColor(R.color.black)
                    .drawShadow(true)
                    .cancelable(false)
                    .tintTarget(true)
                    .transparentTarget(true)
                    .targetRadius(60),
                object : TapTargetView.Listener() {
                    override fun onTargetClick(view: TapTargetView?) {
                        super.onTargetClick(view)
                       showPorttap()
                    }
                })
        }

    }

    private fun showPorttap() {
        if(!sharedPref.getBoolean("didShowPrompt",false)){
            TapTargetView.showFor(this.requireActivity(),
                TapTarget.forView(
                    binding.PortEdittext,
                    "Enter the Main Slave Port Number ",
                    "")
                    .outerCircleColor(androidx.appcompat.R.color.material_deep_teal_200)
                    .outerCircleAlpha(0.96f)
                    .targetCircleColor(R.color.white)
                    .titleTextSize(20)
                    .titleTextColor(R.color.white)
                    .descriptionTextSize(10)
                    .descriptionTextColor(R.color.black)
                    .textColor(R.color.black)
                    .textTypeface(Typeface.SANS_SERIF)
                    .dimColor(R.color.black)
                    .drawShadow(true)
                    .cancelable(false)
                    .tintTarget(true)
                    .transparentTarget(true)
                    .targetRadius(60),
                object : TapTargetView.Listener() {
                    override fun onTargetClick(view: TapTargetView?) {
                        super.onTargetClick(view)
                        showLgNametap()
                    }
                })
        }

    }

    private fun showLgNametap() {
        if(!sharedPref.getBoolean("didShowPrompt",false)){
            TapTargetView.showFor(this.requireActivity(),
                TapTarget.forView(
                    binding.LgNameEditText,
                    "Enter the Lg Name  ",
                    "")
                    .outerCircleColor(androidx.appcompat.R.color.material_deep_teal_200)
                    .outerCircleAlpha(0.96f)
                    .targetCircleColor(R.color.white)
                    .titleTextSize(20)
                    .titleTextColor(R.color.white)
                    .descriptionTextSize(10)
                    .descriptionTextColor(R.color.black)
                    .textColor(R.color.black)
                    .textTypeface(Typeface.SANS_SERIF)
                    .dimColor(R.color.black)
                    .drawShadow(true)
                    .cancelable(false)
                    .tintTarget(true)
                    .transparentTarget(true)
                    .targetRadius(60),
                object : TapTargetView.Listener() {
                    override fun onTargetClick(view: TapTargetView?) {
                        super.onTargetClick(view)
                        showPasswordtap()
                    }
                })
        }

    }

    private fun showPasswordtap() {
        if(!sharedPref.getBoolean("didShowPrompt",false)){
            TapTargetView.showFor(this.requireActivity(),
                TapTarget.forView(
                    binding.LgPassword,
                    "Enter the Main Slave Password ",
                    "")
                    .outerCircleColor(androidx.appcompat.R.color.material_deep_teal_200)
                    .outerCircleAlpha(0.96f)
                    .targetCircleColor(R.color.white)
                    .titleTextSize(20)
                    .titleTextColor(R.color.white)
                    .descriptionTextSize(10)
                    .descriptionTextColor(R.color.black)
                    .textColor(R.color.black)
                    .textTypeface(Typeface.SANS_SERIF)
                    .dimColor(R.color.black)
                    .drawShadow(true)
                    .cancelable(false)
                    .tintTarget(true)
                    .transparentTarget(true)
                    .targetRadius(60),
                object : TapTargetView.Listener() {
                    override fun onTargetClick(view: TapTargetView?) {
                        super.onTargetClick(view)
                        val prefEditor = sharedPref.edit()
                        prefEditor.putBoolean("didShowPrompt", true)
                        prefEditor.apply()
                    }
                })
        }

    }

}