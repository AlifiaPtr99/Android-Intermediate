package com.dicoding.bangkit.storyappdicoding.viewActivity

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.bangkit.storyappdicoding.R
import com.dicoding.bangkit.storyappdicoding.TokenPreference
import com.dicoding.bangkit.storyappdicoding.api.ApiConfig
import com.dicoding.bangkit.storyappdicoding.databinding.ActivityLoginBinding
import com.dicoding.bangkit.storyappdicoding.model.LoginResponse
import com.dicoding.bangkit.storyappdicoding.viewModel.TokenViewModel
import com.dicoding.bangkit.storyappdicoding.viewModel.ViewModelFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private lateinit var tokenViewModel: TokenViewModel
    private lateinit var binding: ActivityLoginBinding

    companion object {
        private const val TAG = "Login Activity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = TokenPreference.getInstance(dataStore)
        tokenViewModel = ViewModelProvider(this, ViewModelFactory(pref))[TokenViewModel::class.java]

        binding.etEmail.inputData = "email"
        binding.etPassword.inputData = "password"

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            login(email, password)
        }
        binding.btnSignup.setOnClickListener{
            Intent(this, RegisterActivity::class.java).also {
                startActivity(it)
            }
        }
        setupView()
    }


    private fun login(email: String, password: String) {
        showProgressBar(true)

        val client = ApiConfig.getApiService().login(email, password)
        client.enqueue(object: Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                showProgressBar(false)
                val responseBody = response.body()
                Log.d(TAG, "onResponse: $responseBody")

                if(response.isSuccessful && responseBody?.message == "success") {
                    tokenViewModel.saveTokens(responseBody.loginResult.token)
                    Toast.makeText(this@LoginActivity, getString(R.string.login_success), Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Log.e(TAG, "onFailure1: ${response.message()}")
                    Toast.makeText(this@LoginActivity, getString(R.string.login_fail), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                showProgressBar(false)
                Log.e(TAG, "onFailure2: ${t.message}")
                Toast.makeText(this@LoginActivity, getString(R.string.login_fail), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun showProgressBar(state: Boolean) {
        if (state) {
            binding.pBar.visibility = View.VISIBLE
        } else {
            binding.pBar.visibility = View.GONE
        }
    }
}