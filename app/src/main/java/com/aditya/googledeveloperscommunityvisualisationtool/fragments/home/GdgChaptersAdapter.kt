package com.aditya.googledeveloperscommunityvisualisationtool.fragments.home

import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.aditya.googledeveloperscommunityvisualisationtool.databinding.GdgChaptersListBinding
import com.bumptech.glide.Glide
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapterCompleteDetails.ChapterEntity

class GdgChaptersAdapter(var chapterList: List<ChapterEntity>):RecyclerView.Adapter<GdgChaptersAdapter.MyViewHolder>() {
    private lateinit var mListen:onItemClickListener

    interface  onItemClickListener {
         fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener:onItemClickListener){
        mListen=listener
    }

    class MyViewHolder(val binding: GdgChaptersListBinding, listener: onItemClickListener):RecyclerView.ViewHolder(binding.root) {

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
                binding.GdgNameTextView.text =this.gdgName
                binding.GdgCityTextView.text = this.city_name
                binding.GDGCountryTextView.text=this.country
                if(this.banner.path.isNotEmpty()){
                    Glide.with(binding.root)
                        .load(this.banner.path)
                        .centerCrop()
                        .into(binding.gdgImage)
                }else{
                    Glide.with(binding.root)
                        .load("https://res.cloudinary.com/startup-grind/image/upload/c_fill,dpr_2.0,f_auto,g_center,h_192,q_auto:good,w_192/v1/gcs/platform-data-goog/contentbuilder/favicon_BWRrca0.png")
                        .centerCrop()
                        .into(binding.gdgImage)
                }
                binding.GdgNameTextView.setSelected(true)
                binding.GdgCityTextView.setSelected(true)
                binding.GDGCountryTextView.setSelected(true)



            }
        }
    }

    fun refreshData(chapterList:List<ChapterEntity>){
        this.chapterList=chapterList
        notifyDataSetChanged()

    }

}

