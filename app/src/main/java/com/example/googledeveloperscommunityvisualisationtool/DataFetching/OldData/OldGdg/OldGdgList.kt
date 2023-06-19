package com.example.googledeveloperscommunityvisualisationtool.DataFetching.OldData.OldGdg

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.googledeveloperscommunityvisualisationtool.databinding.FragmentOldGdgListBinding
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.OldData.OldGDGEntity
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.OldData.OldGDGdatabaseViewModel
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.OldData.oldGDGDatabaseViewModelFactory
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
    private lateinit var oldGdgDatabaseViewModel:OldGDGdatabaseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentOldGdgListBinding.inflate(layoutInflater,container,false)
        val view=binding.root
        recyclerView=binding.recyclerView2
        oldgdgAdapter= oldgdgAdapter(listOf())
        recyclerView.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        recyclerView.adapter=oldgdgAdapter
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val repo=OldGdgRepository(requireContext())

        viewModel = ViewModelProvider(this,OldGdgViewModelFactory(repo,requireContext())).get(OldGdgListViewModel::class.java)

        oldGdgDatabaseViewModel=ViewModelProvider(this,oldGDGDatabaseViewModelFactory(requireContext())).get(OldGDGdatabaseViewModel::class.java)

        checkDatabase()


    }

    private fun checkDatabase() {
        oldGdgDatabaseViewModel.readAllOldGDGViewModel.observe(viewLifecycleOwner, Observer {
            if(it.isEmpty()){
                networkCheck()
            }else{
                loadDataFromdatabase()
            }
        })
    }

    private fun loadDataFromdatabase() {
        oldGdgDatabaseViewModel.readAllOldGDGViewModel.observe(viewLifecycleOwner, Observer {
            oldgdgAdapter.refreshdata(it)
                oldgdgAdapter.setOnItemClickListener(object :oldgdgAdapter.onItemClickListener{
                    override fun onItemClick(position: Int) {
                        val action=OldGdgListDirections.actionOldGdgListToOldEvent(it[position])
                        findNavController().navigate(action)
                    }

                })
        })
    }

    private fun networkCheck() {
        if(viewModel.isNetworkAvailable()){
            getAllOldGDG()
        }else{
            Toast.makeText(requireContext(),"No Network Available!! Please Try Again... ",Toast.LENGTH_SHORT).show()

        }
    }

    private fun getAllOldGDG() {
        val job=CoroutineScope(Dispatchers.IO).launch {
            viewModel.getGdgdata()
        }
        CoroutineScope(Dispatchers.IO).launch {
            job.join()
            delay(2000)
            withContext(Dispatchers.Main){
                gdglist=viewModel.returnlist()
                val databaseList=convertDataType(gdglist)
                //storing in database
                for( gdg in databaseList){
                    oldGdgDatabaseViewModel.addoldGDGViewModel(gdg)
                }
            }
        }
    }

    private fun convertDataType(gdglist: List<oldGdgDataItem>): List<OldGDGEntity> {
        val databaseOldGgd= mutableListOf<OldGDGEntity>()
        for(i in gdglist){
            val list=OldGDGEntity(i.city,i.country,i.id,i.lat,i.lon,i.name,i.state,i.status,i.urlname)
            databaseOldGgd.add(list)
        }
        return databaseOldGgd
    }


}