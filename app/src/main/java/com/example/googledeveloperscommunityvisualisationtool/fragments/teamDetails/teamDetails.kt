package com.example.googledeveloperscommunityvisualisationtool.fragments.teamDetails

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.googledeveloperscommunityvisualisationtool.R

class teamDetails : Fragment() {

    companion object {
        fun newInstance() = teamDetails()
    }

    private lateinit var viewModel: TeamDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_team_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TeamDetailsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}