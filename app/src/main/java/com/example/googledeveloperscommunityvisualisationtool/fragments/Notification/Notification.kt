package com.example.googledeveloperscommunityvisualisationtool.fragments.Notification

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.googledeveloperscommunityvisualisationtool.R
import com.example.googledeveloperscommunityvisualisationtool.databinding.FragmentNotificationBinding
import com.example.googledeveloperscommunityvisualisationtool.fragments.home.GdgChaptersAdapter
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.notificationRoom.NotifyEntity
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.notificationRoom.NotifyViewModel
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.notificationRoom.NotifyViewModelFactory

class Notification : Fragment() {
    lateinit var binding:FragmentNotificationBinding
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
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = notificationAdapter

        noItemText.visibility=View.VISIBLE
        recyclerView.visibility=View.GONE
        clearAll.visibility=View.GONE


        return view
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