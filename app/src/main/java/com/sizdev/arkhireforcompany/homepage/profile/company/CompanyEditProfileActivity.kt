package com.sizdev.arkhireforcompany.homepage.profile.company

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.databinding.ActivityCompanyEditProfileBinding
import java.io.IOException
import java.util.*

class CompanyEditProfileActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener, GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnCameraIdleListener, GoogleMap.OnMarkerClickListener {

    lateinit var binding: ActivityCompanyEditProfileBinding
    lateinit var locationManager: LocationManager
    private var locationGps: Location? = null
    private var locationDefault = LatLng(-6.200000, 106.816666)
    private lateinit var markerDefault: Marker


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_company_edit_profile)


        val sharedPrefDataLocation = this.getSharedPreferences("companyLocation", Context.MODE_PRIVATE)
        val savedLocationLatitude = sharedPrefDataLocation.getString("latitude", null)
        val savedLocationLongitude = sharedPrefDataLocation.getString("longitude", null)

        if(savedLocationLatitude !=null && savedLocationLongitude != null) {
            locationDefault = LatLng(savedLocationLatitude.toDouble(), savedLocationLongitude.toDouble())
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.editCompanyLocationmap) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.etEditCompanyLocation.setOnClickListener {
            saveCurrentLocation()
        }

        binding.btNewProfileDone.setOnClickListener {
            val intent = Intent(this, CompanyProfileActivity::class.java)
            updateCompanyData()
            Toast.makeText(this, "Profile Updated", Toast.LENGTH_LONG).show()
            startActivity(intent)
            finish()
        }
    }

    private fun updateCompanyData() {
        val companyName = binding.etEditCompanyName.text.toString()
        val companyType = binding.etEditCompanyType.text.toString()

        val sharedPrefData = getSharedPreferences("fullData", Context.MODE_PRIVATE)
        val editor = sharedPrefData.edit()
        editor.apply {
            putString("companyName", companyName)
            putString("companyType", companyType)
        }.apply()

    }

    private fun saveCurrentLocation() {

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0F
        ) { location ->
            locationGps = location
            val latitude = locationGps!!.latitude.toString()
            val longitude = locationGps!!.longitude.toString()
            binding.tvCurrentAddress.text = "$latitude, $longitude"
            val sharedPrefData = getSharedPreferences("companyLocation", Context.MODE_PRIVATE)
            val editor = sharedPrefData.edit()
            editor.apply {
                putString("latitude", latitude)
                putString("longitude", longitude)
            }.apply()
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isMyLocationButtonEnabled = true
        googleMap.mapType = GoogleMap.MAP_TYPE_TERRAIN

        markerDefault = googleMap.addMarker(
            MarkerOptions()
                .position(locationDefault)
        )

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationDefault, 16f))
        googleMap.setOnMarkerClickListener(this)
    }

    override fun onLocationChanged(location: Location?) {
        val geocoder = Geocoder(this, Locale.getDefault())
        var addresses: List<Address>? = null

        try {
            addresses = geocoder.getFromLocation(location!!.latitude, location!!.longitude, 1)
        }catch (e: IOException){
            e.printStackTrace()
        }

        setAddress(addresses!![0])

    }

    @SuppressLint("SetTextI18n")
    private fun setAddress(addresses: Address) {
        if (addresses!=null){
            if(addresses.getAddressLine(0) != null){
                binding.tvCurrentAddress.text = (addresses.getAddressLine(0))
            }
            if (addresses.getAddressLine(1) != null) {
                binding.tvCurrentAddress.text = binding.tvCurrentAddress.text.toString() + addresses.getAddressLine(1)
            }
        }
    }

    override fun onCameraMove() {

    }

    override fun onCameraMoveStarted(camera: Int) {

    }

    override fun onCameraIdle() {

    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        return false
    }
}