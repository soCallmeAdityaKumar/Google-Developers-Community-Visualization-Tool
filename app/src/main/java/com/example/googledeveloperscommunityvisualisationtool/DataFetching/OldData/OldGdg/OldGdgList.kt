package com.example.googledeveloperscommunityvisualisationtool.DataFetching.OldData.OldGdg

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.googledeveloperscommunityvisualisationtool.databinding.FragmentOldGdgListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OldGdgList : Fragment() {
    private lateinit var binding:FragmentOldGdgListBinding
    private lateinit var viewModel: OldGdgListViewModel
    private lateinit var  oldgdgAdapter:oldgdgAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var gdglist:List<oldGdgDataItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentOldGdgListBinding.inflate(layoutInflater,container,false)
        val view=binding.root
        recyclerView=binding.recyclerView2
        gdglist= listOf()
        oldgdgAdapter= oldgdgAdapter(gdglist)
        recyclerView.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        recyclerView.adapter=oldgdgAdapter
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val repo=OldGdgRepository(requireContext())
        viewModel = ViewModelProvider(this,OldGdgViewModelFactory(repo,requireContext())).get(OldGdgListViewModel::class.java)

        Log.d("oldgdg","befor job")
        val job=CoroutineScope(Dispatchers.IO).launch {
            viewModel.getGdgdata()
            Log.d("oldgdg","after .getgdgdata")
        }
        CoroutineScope(Dispatchers.IO).launch {
            job.join()
            delay(2000)
            Log.d("oldgdg","after job.join")
            withContext(Dispatchers.Main){
                gdglist=viewModel.returnlist()
                Log.d("oldgdg","gdglist ${gdglist.size}")
                oldgdgAdapter.refreshdata(gdglist)
                oldgdgAdapter.setOnItemClickListener(object :oldgdgAdapter.onItemClickListener{
                    override fun onItemClick(position: Int) {

                    }

                })
            }
        }
    }

}