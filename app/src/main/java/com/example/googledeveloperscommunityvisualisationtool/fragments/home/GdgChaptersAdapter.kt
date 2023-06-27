package com.example.googledeveloperscommunityvisualisationtool.fragments.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.googledeveloperscommunityvisualisationtool.databinding.GdgChaptersListBinding
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapterCompleteDetails.ChapterEntity

class GdgChaptersAdapter(var chapterList: List<ChapterEntity>):RecyclerView.Adapter<GdgChaptersAdapter.MyViewHolder>() {

    private lateinit var mListen:onItemClickListener
    interface  onItemClickListener {
         fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener:onItemClickListener){
        mListen=listener
    }
    class MyViewHolder(val binding:GdgChaptersListBinding,listener: onItemClickListener):RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding= GdgChaptersListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding,mListen)
    }

    override fun getItemCount(): Int {
        return chapterList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder) {
            with(chapterList[position]) {
                binding.GdgNameTextView.text = this.gdgName
                binding.GdgCityTextView.text = this.city_name
                binding.GDGCountryTextView.text=this.country
            }
        }
    }
    fun refreshData(chapterList:List<ChapterEntity>){
        this.chapterList=chapterList
        notifyDataSetChanged()

    }
}

