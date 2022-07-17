package com.dicoding.bangkit.storyappdicoding.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.bangkit.storyappdicoding.viewActivity.DetailStoryActivity
import com.dicoding.bangkit.storyappdicoding.databinding.ItemListStoryBinding
import com.dicoding.bangkit.storyappdicoding.model.ItemStory

class RecyclerViewAdapter(private val listUser: ArrayList<ItemStory>): RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int = listUser.size


    class ViewHolder(private val binding: ItemListStoryBinding)  : RecyclerView.ViewHolder(binding.root){
        fun bind(storyModel: ItemStory){
            with(binding){
                tvName.text = storyModel.name
                tvDesc.text = storyModel.description
                Glide.with(binding.root.context)
                    .load(storyModel.photoUrl)
                    .circleCrop()
                    .into(imgHome)

                binding.root.setOnClickListener {
                    val intent = Intent(binding.root.context, DetailStoryActivity::class.java)
                    intent.putExtra("EXTRA_STORY", storyModel)
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            binding.root.context as Activity,
                            Pair(binding.imgHome, "img_trans_detail"),
                            Pair(binding.tvName, "tv_name_detail"),
                            Pair(binding.tvDesc, "tv_desc_detail"),
                        )
                    binding.root.context.startActivity(intent,optionsCompat.toBundle())
                }
            }
        }

    }
}