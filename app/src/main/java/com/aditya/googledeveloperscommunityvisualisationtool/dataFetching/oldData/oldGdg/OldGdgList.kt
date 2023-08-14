package com.aditya.googledeveloperscommunityvisualisationtool.dataFetching.oldData.oldGdg

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.car.ui.recyclerview.CarUiListItemAdapterAdapterV1.ViewHolderWrapper
import com.aditya.googledeveloperscommunityvisualisationtool.MainActivity
import com.aditya.googledeveloperscommunityvisualisationtool.R
import com.aditya.googledeveloperscommunityvisualisationtool.databinding.FragmentOldGdgListBinding
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.OldData.OldGDGEntity
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.OldData.oldGDGroomViewModel
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.OldData.oldGDGroomFactory
import com.aditya.googledeveloperscommunityvisualisationtool.utility.ConstantPrefs
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class OldGdgList : Fragment() {
    private lateinit var binding: FragmentOldGdgListBinding
    private lateinit var viewModel: OldGdgListViewModel
    private lateinit var  oldgdgAdapter:oldgdgAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var gdglist:List<oldGdgDataItem>
    private lateinit var oldGdgDatabaseViewModel:oldGDGroomViewModel
    lateinit var progressBar: ProgressBar
    lateinit var searchView:androidx.appcompat.widget.SearchView
    lateinit var recyclerViewcardView:CardView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentOldGdgListBinding.inflate(layoutInflater,container,false)
        val view=binding.root

        progressBar=binding.progressBar
        recyclerView=binding.recyclerView2
        searchView=binding.searchView

        progressBar.visibility=View.VISIBLE
        recyclerView.visibility=View.GONE

        oldgdgAdapter= oldgdgAdapter(listOf())
        recyclerView.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        recyclerView.adapter=oldgdgAdapter

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val repo=OldGdgRepository(requireContext())

        viewModel = ViewModelProvider(this,OldGdgViewModelFactory(repo,requireContext())).get(OldGdgListViewModel::class.java)

        oldGdgDatabaseViewModel=ViewModelProvider(this,oldGDGroomFactory(requireContext())).get(oldGDGroomViewModel::class.java)

        checkDatabase()


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
            if(progressBar.visibility==View.VISIBLE)progressBar.visibility=View.GONE
            if(recyclerView.visibility==View.GONE)recyclerView.visibility=View.VISIBLE

            oldgdgAdapter.refreshdata(it)
            val newAdapterList=it.toMutableList()
            searchView.setOnQueryTextListener(object :
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    newAdapterList.clear()
                    val searchtext = newText!!.toLowerCase(Locale.getDefault())
                    if (searchtext.isNotEmpty()) {
                        it.forEach {

                            if (it.name.lowercase(Locale.getDefault()).contains(searchtext)||
                                it.city.lowercase(Locale.getDefault()).contains(searchtext)||
                                it.country.lowercase(Locale.getDefault()).contains(searchtext)) {
                                newAdapterList.add(it)
                            }
                        }
                        oldgdgAdapter.refreshdata(newAdapterList)
                    } else {
                        newAdapterList.clear()
                        newAdapterList.addAll(it)
                        oldgdgAdapter.refreshdata(newAdapterList)
                    }
                    return false
                }
            })
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


}