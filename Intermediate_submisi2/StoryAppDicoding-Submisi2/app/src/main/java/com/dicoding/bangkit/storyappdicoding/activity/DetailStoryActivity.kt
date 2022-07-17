package com.dicoding.bangkit.storyappdicoding.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.dicoding.bangkit.storyappdicoding.activity.models.ItemStory
import com.dicoding.bangkit.storyappdicoding.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val dataStory: ItemStory? = intent.getParcelableExtra("EXTRA_STORY")

        if (dataStory != null) {
            binding.tvName.text = dataStory.name
            binding.tvIdStory.text = dataStory.id
            binding.tvCreatedAt.text = "Created at ${ dataStory.createdAt}"


            Glide.with(this)
                .load(dataStory.photoUrl)
                .circleCrop()
                .into(binding.imgDetail)

            binding.tvDesc.text = dataStory.description
        }

    }
}