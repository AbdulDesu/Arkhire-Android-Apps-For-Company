package com.sizdev.arkhireforcompany.homepage.profile.company


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.databinding.ActivityCompanyProfileBinding

class CompanyProfileActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var binding: ActivityCompanyProfileBinding
    private var defaultLocation = LatLng(-6.200000, 106.816666)
    private lateinit var markerDefault: Marker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_company_profile)

        val sharedPrefData = this.getSharedPreferences("fullData", Context.MODE_PRIVATE)
        val sharedPrefDataLocation = this.getSharedPreferences("companyLocation", Context.MODE_PRIVATE)

        //Get Company Data
        val savedCompanyName = sharedPrefData.getString("companyName", "Traveloka ID")
        val savedCompanyType = sharedPrefData.getString("companyType", "Enterprise Company")
        val savedLocationLatitude = sharedPrefDataLocation.getString("latitude", null)
        val savedLocationLongitude = sharedPrefDataLocation.getString("longitude", null)
        val lorem: String = getString(R.string.lorem_ipsum)

        if(savedLocationLatitude !=null && savedLocationLongitude != null) {
            defaultLocation = LatLng(savedLocationLatitude.toDouble(), savedLocationLongitude.toDouble())
        }

        binding.tvCompanyProfileName.text = savedCompanyName
        binding.tvCompanyType.text = savedCompanyType
        binding.tvCompanyDescription.text = lorem

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //RecyclerViewAdapter Manager
        binding.rvCompanyLookingFor.adapter = CompanyLookingForAdapter()
        binding.rvCompanyLookingFor.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        binding.btEditCompanyProfile.setOnClickListener {
            val intent = Intent(this, CompanyEditProfileActivity::class.java)
            startActivity(intent)
        }
        
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.mapType = GoogleMap.MAP_TYPE_TERRAIN

            markerDefault = googleMap.addMarker(
                MarkerOptions()
                    .position(defaultLocation)
                    .title(binding.tvCompanyProfileName.text as String?)
            )
            markerDefault.tag = 0

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 16f))
            googleMap.setOnMarkerClickListener(this)
        }

        override fun onMarkerClick(marker: Marker): Boolean {
            val clickCount = marker.tag as? Int

            clickCount?.let {
                val newClickCount = it + 1
                marker.tag = newClickCount
                Toast.makeText(
                    this,
                    "${marker.title} has been clicked $newClickCount times.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            return false
        }


}