package com.dicoding.bangkit.storyappdicoding.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.bangkit.storyappdicoding.R
import com.dicoding.bangkit.storyappdicoding.activity.api.ApiConfig
import com.dicoding.bangkit.storyappdicoding.activity.camera.CameraActivity
import com.dicoding.bangkit.storyappdicoding.activity.camera.reduceFileImage
import com.dicoding.bangkit.storyappdicoding.activity.camera.rotateBitmap
import com.dicoding.bangkit.storyappdicoding.activity.camera.uriToFile
import com.dicoding.bangkit.storyappdicoding.activity.viewmodel.TokenPreference
import com.dicoding.bangkit.storyappdicoding.databinding.ActivityAddStoryBinding
import com.dicoding.bangkit.storyappdicoding.activity.models.AddStoryResponse
import com.dicoding.bangkit.storyappdicoding.activity.models.UserSession
import com.dicoding.bangkit.storyappdicoding.activity.viewmodel.TokenViewModel
import com.dicoding.bangkit.storyappdicoding.viewModel.ViewModelFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddStoryActivity : AppCompatActivity() {


    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private lateinit var tokenViewModel: TokenViewModel
    private lateinit var binding: ActivityAddStoryBinding
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (supportActionBar != null) {
            (supportActionBar as ActionBar).title = "New Story"
        }
        supportActionBar?.setDisplayShowTitleEnabled(true)

        setupViewModel()

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.btnCamera.setOnClickListener { startCameraX() }
        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnUpload.setOnClickListener { uploadImage() }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setupViewModel() {
        tokenViewModel = ViewModelProvider(
            this,
            ViewModelFactory(TokenPreference.getInstance(dataStore))
        )[TokenViewModel::class.java]
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    getString(R.string.permission_denied),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            getFile = myFile
            val result = rotateBitmap(
                BitmapFactory.decodeFile(getFile?.path),
                isBackCamera
            )

            binding.imgPreview.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri

            val myFile = uriToFile(selectedImg, this@AddStoryActivity)

            getFile = myFile

            binding.imgPreview.setImageURI(selectedImg)
        }
    }


    private fun uploadImage() {
        showProgressBar(true)
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)

            val description =
                binding.etAddDesc.text.toString().toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            tokenViewModel.getTokens().observe(this) {
                if (it != null) {
                    val client = ApiConfig.getApiService()
                        .uploadStory("Bearer " + it.token, description, imageMultipart)
                    client.enqueue(object : Callback<AddStoryResponse> {
                        override fun onResponse(
                            call: Call<AddStoryResponse>,
                            response: Response<AddStoryResponse>
                        ) {
                            showProgressBar(false)

                            val responseBody = response.body()
                            Log.d(TAG, "onResponse: $responseBody")

                            if (response.isSuccessful) {
                                val responseBody = response.body()
                                if (responseBody != null) {
                                    Toast.makeText(
                                        this@AddStoryActivity,
                                        responseBody.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val intent = Intent(this@AddStoryActivity, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            } else {
                                Toast.makeText(
                                    this@AddStoryActivity,
                                    response.message(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<AddStoryResponse>, t: Throwable) {
                            showProgressBar(false)
                            Toast.makeText(
                                this@AddStoryActivity,
                                "Failed to create story",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    })
                }
            }


        }

    }
    private fun showProgressBar(state: Boolean) {
        if (state) {
            binding.pBar.visibility = View.VISIBLE
        } else {
            binding.pBar.visibility = View.GONE
        }
    }

    companion object {
        const val TAG = "AddStoryActivity"
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}


