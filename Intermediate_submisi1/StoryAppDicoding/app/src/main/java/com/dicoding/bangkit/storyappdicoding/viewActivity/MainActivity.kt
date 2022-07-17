package com.dicoding.bangkit.storyappdicoding.viewActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.bangkit.storyappdicoding.R
import com.dicoding.bangkit.storyappdicoding.TokenPreference
import com.dicoding.bangkit.storyappdicoding.databinding.ActivityMainBinding
import com.dicoding.bangkit.storyappdicoding.model.ItemStory
import com.dicoding.bangkit.storyappdicoding.viewModel.StoryViewModel
import com.dicoding.bangkit.storyappdicoding.viewModel.TokenViewModel
import com.dicoding.bangkit.storyappdicoding.viewModel.ViewModelFactory
import com.dicoding.bangkit.storyappdicoding.adapter.RecyclerViewAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var tokenViewModel: TokenViewModel
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userToken")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val pref = TokenPreference.getInstance(dataStore)
        tokenViewModel = ViewModelProvider(this, ViewModelFactory(pref))[TokenViewModel::class.java]

        val storyViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[StoryViewModel::class.java]
        tokenViewModel.getTokens().observe(this
        ) { token: String? ->
            if (token != null){
                storyViewModel.setListStory(token)

            } else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            }
        }
        storyViewModel.storyList.observe(this) { storyList ->
            getStoryList(storyList)
        }
        storyViewModel.isLoading.observe(this) {
            showProgressBar(it)
        }

        binding.fabAdd.setOnClickListener {
            Intent(this, AddStoryActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_logout, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.m_logout -> {
                tokenViewModel.removeTokens()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getStoryList(listStory: ArrayList<ItemStory>) {

        val adapter = RecyclerViewAdapter(listStory)
        binding.rvListStory.adapter = adapter
        binding.rvListStory.layoutManager = LinearLayoutManager(this)

    }

    private fun showProgressBar(state: Boolean) {
        if (state) {
            binding.pBar.visibility = View.VISIBLE
        } else {
            binding.pBar.visibility = View.GONE
        }
    }

}
