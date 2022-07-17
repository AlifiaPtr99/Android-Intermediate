package com.dicoding.bangkit.storyappdicoding.activity

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.dicoding.bangkit.storyappdicoding.R
import com.dicoding.bangkit.storyappdicoding.activity.utils.HelperObject
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.dicoding.bangkit.storyappdicoding.databinding.ActivityMapsBinding
import com.dicoding.bangkit.storyappdicoding.viewModel.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {


    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var latMaps: ArrayList<LatLng>? = null
    private var latName: ArrayList<String>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        supportActionBar!!.title = "User Story Maps"

    }

     override fun onMapReady(googleMap: GoogleMap) {
         mMap = googleMap
         mMap.uiSettings.isZoomControlsEnabled = true
         mMap.uiSettings.isIndoorLevelPickerEnabled = true
         mMap.uiSettings.isCompassEnabled = true
         mMap.uiSettings.isMapToolbarEnabled = true

         latMaps = intent.getParcelableArrayListExtra(HelperObject.EXTRA_MAP)
         latName = intent.getStringArrayListExtra(HelperObject.EXTRA_MAP_NAME)

         for (i in latMaps!!.indices) {
             mMap.addMarker(
                 MarkerOptions()
                     .position(latMaps!![i])
                     .title(latName!![i])
             )
             mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latMaps!![i], 15f))
         }


         mMap.setOnMapLongClickListener { latLng ->
             mMap.addMarker(
                 MarkerOptions()
                     .position(latLng)
                     .title("New Marker")
                     .snippet("Lat: ${latLng.latitude} Long: ${latLng.longitude}")
                     .icon(vectorToBitmap(R.drawable.v_maps, Color.parseColor("#274C77")))
             )
         }

         mMap.setOnPoiClickListener { pointOfInterest ->
             val poiMarker = mMap.addMarker(
                 MarkerOptions()
                     .position(pointOfInterest.latLng)
                     .title(pointOfInterest.name)
                     .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
             )
             poiMarker?.showInfoWindow()
         }
     }

     private fun vectorToBitmap(@DrawableRes id: Int, @ColorInt color: Int): BitmapDescriptor {
         val vectorDrawable = ResourcesCompat.getDrawable(resources, id, null)
         if (vectorDrawable == null) {
             Log.e("BitmapHelper", getString(R.string.src_not_found))
             return BitmapDescriptorFactory.defaultMarker()
         }
         val bitmap = Bitmap.createBitmap(
             vectorDrawable.intrinsicWidth,
             vectorDrawable.intrinsicHeight,
             Bitmap.Config.ARGB_8888
         )
         val canvas = Canvas(bitmap)
         vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
         DrawableCompat.setTint(vectorDrawable, color)
         vectorDrawable.draw(canvas)

         return BitmapDescriptorFactory.fromBitmap(bitmap)
     }





 }


