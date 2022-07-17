package com.dicoding.bangkit.storyappdicoding.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.bangkit.storyappdicoding.R
import com.dicoding.bangkit.storyappdicoding.activity.adapter.LoadingStateAdapter
import com.dicoding.bangkit.storyappdicoding.activity.adapter.StoryPagingAdapter
import com.dicoding.bangkit.storyappdicoding.activity.models.ItemStory
import com.dicoding.bangkit.storyappdicoding.activity.utils.HelperObject
import com.dicoding.bangkit.storyappdicoding.activity.viewmodel.*
import com.dicoding.bangkit.storyappdicoding.databinding.ActivityMainBinding
import com.dicoding.bangkit.storyappdicoding.viewModel.ViewModelFactory
import com.google.android.gms.maps.model.LatLng

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var vmMain: MainViewModel
    private lateinit var storyAdapter: StoryPagingAdapter
    private val vmStory: StoryViewModel by viewModels { FactoryViewModel(this) }
    private var latMap: ArrayList<LatLng>? = null
    private var latName: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.title = "Story User"

        setViewModel()
        setSwipeRefreshLayout()

        storyAdapter= StoryPagingAdapter()
        latMap = ArrayList()
        latName = ArrayList()
        storyAdapter.setOnItemClickCallback(object : StoryPagingAdapter.OnItemClickCallback {
            override fun itemClicked(storyUser: ItemStory) {
                Intent(this@MainActivity, DetailStoryActivity::class.java).also {
                    it.putExtra(HelperObject.EXTRA_NAME, storyUser.name)
                    it.putExtra(HelperObject.EXTRA_DESCRIPTION, storyUser.description)
                    it.putExtra(HelperObject.EXTRA_PHOTO, storyUser.photoUrl)
                    startActivity(it)
                }
            }
        })

        showProgressBar(true)
        binding.swipeRefresh.isRefreshing = true
        vmMain.getStory().observe(this) {
            if (it != null) {
                for (i in it.indices) {
                    latMap!!.add(LatLng(it[i].lat, it[i].lon))
                    latName!!.add(it[i].name)
                   showProgressBar(false)
                    binding.swipeRefresh.isRefreshing = false
                }
            }
        }

    }

    private fun getUserToken(token: String) {
        binding.apply {
            if (token.isEmpty()) return
            showProgressBar(true)
           vmMain.setListStory(token)
        }
    }

    private fun setViewModel() {
        vmMain = ViewModelProvider(this, ViewModelFactory(TokenPreference.getInstance(dataStore)))[MainViewModel::class.java]
        vmMain.getUserSession().observe(this) { user ->
            if (user.isLogin) {
                HelperObject.token = "Bearer ${user.token}"
                binding.apply {
                    rvListStory.layoutManager = LinearLayoutManager(this@MainActivity)
                    rvListStory.adapter = storyAdapter.withLoadStateFooter(
                        footer = LoadingStateAdapter{
                            storyAdapter.retry()
                        }
                    )
                    getUserToken(HelperObject.token)
                }

                vmStory.storyUser().observe(this) { story ->
                    storyAdapter.submitData(lifecycle, story)
                }
                binding.fabAdd.setOnClickListener { view ->
                        if (view.id == R.id.fab_add) {
                    startActivity(
                        Intent(this@MainActivity, AddStoryActivity::class.java).apply {
                            putExtra(HelperObject.EXTRA_TOKEN, HelperObject.token)
                        }
                    )
                        }

                }

            } else{
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }


        override fun onCreateOptionsMenu(menu: Menu): Boolean {
            val inflater = menuInflater
            inflater.inflate(R.menu.menu_logout, menu)
            return super.onCreateOptionsMenu(menu)
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
             when (item.itemId) {
                R.id.m_logout -> {
                    vmMain.logout()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.m_maps -> {
                    val mapsIntent = Intent(this, MapsActivity::class.java)
                    mapsIntent.putExtra(HelperObject.EXTRA_MAP, latMap)
                    mapsIntent.putExtra(HelperObject.EXTRA_MAP_NAME, latName)
                    startActivity(mapsIntent)
                }
            }
            return true
        }



        private fun showProgressBar(state: Boolean) {
            if (state) {
                binding.pBar.visibility = View.VISIBLE
            } else {
                binding.pBar.visibility = View.GONE
            }
        }

        private fun setSwipeRefreshLayout() {
            binding.swipeRefresh.setOnRefreshListener {
                showProgressBar(false)
            }
        }



    }

