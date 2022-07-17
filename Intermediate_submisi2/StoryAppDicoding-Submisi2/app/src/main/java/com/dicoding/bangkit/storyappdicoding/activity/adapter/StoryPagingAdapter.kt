package com.dicoding.bangkit.storyappdicoding.activity.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.bangkit.storyappdicoding.activity.DetailStoryActivity
import com.dicoding.bangkit.storyappdicoding.activity.models.ItemStory
import com.dicoding.bangkit.storyappdicoding.databinding.ItemListStoryBinding

class StoryPagingAdapter : PagingDataAdapter<ItemStory, StoryPagingAdapter.ViewHolder>(DIFF_CALLBACK) {

    private var itemClickCallback: OnItemClickCallback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
        //holder.bind(listUser[position])
    }

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
                            Pair(binding.tvName, "tv_name_detail"),
                            Pair(binding.tvDesc, "tv_desc_detail"),
                            Pair(binding.imgHome, "img_trans_detail"),
                        )
                    binding.root.context.startActivity(intent,optionsCompat.toBundle())
                }
            }
        }
    }



    fun setOnItemClickCallback(ItemClickCallback: OnItemClickCallback) {
        this.itemClickCallback = ItemClickCallback
    }

    interface OnItemClickCallback {
        fun itemClicked(story: ItemStory)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemStory>() {
            override fun areItemsTheSame(
                oldStory: ItemStory,
                newStory: ItemStory
            ): Boolean {
                return oldStory == newStory
            }

            override fun areContentsTheSame(
                oldStory: ItemStory,
                newStory: ItemStory
            ): Boolean {
                return oldStory.name == newStory.name &&
                        oldStory.description == newStory.description &&
                        oldStory.photoUrl == newStory.photoUrl
            }
        }
    }
}





