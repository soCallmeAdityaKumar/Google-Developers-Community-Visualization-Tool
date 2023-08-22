package com.aditya.googledeveloperscommunityvisualisationtool.fragments.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aditya.googledeveloperscommunityvisualisationtool.databinding.CountrySortListBinding
import com.aditya.googledeveloperscommunityvisualisationtool.databinding.GdgChaptersListBinding
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapterCompleteDetails.ChapterEntity

class CounrtyAdapter(var countryList:List<String>):RecyclerView.Adapter<CounrtyAdapter.myViewHoler>() {

    private lateinit var mListen:onItemClickListener

    interface  onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener:onItemClickListener){
        mListen=listener
    }

    class myViewHoler(val binding:CountrySortListBinding,listener: onItemClickListener):RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHoler {
        val binding= CountrySortListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return myViewHoler(binding,mListen)
    }

    override fun getItemCount(): Int {
        return countryList.size
    }

    override fun onBindViewHolder(holder: myViewHoler, position: Int) {
        with(holder){
            with(countryList[position]){
                binding.countryText.text=this
            }
        }
    }
    fun refreshData(CountryList:List<String>){
        this.countryList=CountryList
        notifyDataSetChanged()

    }

}
