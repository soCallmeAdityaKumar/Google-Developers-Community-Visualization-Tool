package com.aditya.googledeveloperscommunityvisualisationtool.fragments.Notification

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView.RecyclerListener
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aditya.googledeveloperscommunityvisualisationtool.MainActivity
import com.aditya.googledeveloperscommunityvisualisationtool.fragments.home.GdgChaptersAdapter
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.notificationRoom.NotifyEntity
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.notificationRoom.NotifyViewModel
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.notificationRoom.NotifyViewModelFactory
import com.aditya.googledeveloperscommunityvisualisationtool.utility.ConstantPrefs
import com.aditya.googledeveloperscommunityvisualisationtool.R
import com.aditya.googledeveloperscommunityvisualisationtool.databinding.FragmentNotificationBinding

class Notification : Fragment() {
    lateinit var binding: FragmentNotificationBinding
    lateinit var notificationViewmodel:NotifyViewModel
    lateinit var recyclerView:RecyclerView
    lateinit var notificationAdapter: NotifyAdapter
    lateinit var notificationList:ArrayList<notificationData>
    lateinit var noItemText:TextView
    lateinit var clearAll:Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentNotificationBinding.inflate(inflater,container,false)
        val view=binding.root

        clearAll=binding.ClearButtton
        noItemText=binding.NoItemText
        notificationList= arrayListOf()
        recyclerView=binding.NotificationrecyclerView
        notificationAdapter = NotifyAdapter(notificationList)
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = notificationAdapter

        noItemText.visibility=View.VISIBLE
        recyclerView.visibility=View.GONE
        clearAll.visibility=View.GONE


        return view
    }
    override fun onResume() {
        super.onResume()
        loadConnectionStatus()
        val customAppBar = (activity as MainActivity).binding.appBarMain
        val menuButton = customAppBar.menuButton

        val navController = findNavController()
        val isRootFragment = navController.graph.startDestinationId == navController.currentDestination?.id

        if (isRootFragment) {
            menuButton.setBackgroundResource(R.drawable.baseline_menu_24)
//            menuButton?.visibility = View.VISIBLE
//            backButton?.visibility = View.GONE
        } else {
            menuButton.setBackgroundResource(R.drawable.backarrow)
//            menuButton?.visibility = View.GONE
//            backButton?.visibility = View.VISIBLE
            menuButton?.setOnClickListener {
                (activity as MainActivity).onBackPressed()
            }
        }
    }
    private fun loadConnectionStatus() {
        val sharedPreferences = activity?.getSharedPreferences(
            ConstantPrefs.SHARED_PREFS.name,
            Context.MODE_PRIVATE
        )

        val isConnected = sharedPreferences?.getBoolean(ConstantPrefs.IS_CONNECTED.name, false)
        val act=activity as MainActivity
        if (isConnected!!) {
            act.binding.appBarMain.LGConnected.visibility=View.VISIBLE
            act.binding.appBarMain.LGNotConnected.visibility=View.INVISIBLE
        } else {
            act.binding.appBarMain.LGConnected.visibility=View.INVISIBLE
            act.binding.appBarMain.LGNotConnected.visibility=View.VISIBLE
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        notificationViewmodel=ViewModelProvider(requireActivity(),NotifyViewModelFactory(requireContext())).get(NotifyViewModel::class.java)
        notificationViewmodel.readAllNotification.observe(requireActivity(), Observer{
            notificationList=convertListType(it)
            if(notificationList.isNotEmpty()){
                noItemText.visibility=View.GONE
                recyclerView.visibility=View.VISIBLE
                clearAll.visibility=View.VISIBLE
            }
            notificationAdapter.refreshData(notificationList)
        })

        clearAll.setOnClickListener {
            notificationViewmodel.deleteAllNotification()
            noItemText.visibility=View.VISIBLE
            recyclerView.visibility=View.GONE
            clearAll.visibility=View.GONE
        }
    }

    private fun convertListType(it: List<NotifyEntity>?): ArrayList<notificationData> {
        val ls=ArrayList<notificationData>()
        for(i in 0 until it!!.size){
            ls.add(notificationData(it[i].image,it[i].desc,it[i].title,it[i].timeinMiliSec,it[i].time))
        }
        return ls
    }

}