package com.sizdev.arkhireforcompany.homepage.item.account.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.PopupMenu
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
import com.sizdev.arkhireforcompany.homepage.HomeActivity
import com.sizdev.arkhireforcompany.homepage.item.account.profile.edit.CompanyEditProfileActivity
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*

class CompanyProfileActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var binding: ActivityCompanyProfileBinding
    private lateinit var markerDefault: Marker

    private var defaultLocation = LatLng(-6.200000, 106.816666)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_company_profile)

        // Data Loading Management
        binding.loadingScreen.visibility = View.VISIBLE
        binding.progressBar.max = 100

        // Get Saved Data
        val companyID = intent.getStringExtra("companyID")
        val companyName = intent.getStringExtra("companyName")
        val companyType = intent.getStringExtra("companyType")
        val companyImage = intent.getStringExtra("companyImage")
        val companyLinkedin = intent.getStringExtra("companyLinkedin")
        val companyInstagram = intent.getStringExtra("companyInstagram")
        val companyFacebook = intent.getStringExtra("companyFacebook")
        val companyDesc = intent.getStringExtra("companyDesc")
        val companyLatitude = intent.getStringExtra("companyLatitude")
        val companyLongitude = intent.getStringExtra("companyLongitude")

        // Set Map
        if (companyLatitude != "null" && companyLongitude != "null") {
            if (companyLatitude != null && companyLongitude != null) {
                defaultLocation = LatLng(companyLatitude.toDouble(), companyLongitude.toDouble())
            }
        }

        // Set Company Data
        binding.tvCompanyProfileName.text = companyName
        binding.tvCompanyType.text = companyType
        binding.tvCompanyDescription.text = companyDesc

        // Set Background
        when (companyType) {
            "Enterprise" -> binding.ivCompanyProfileCover.setImageResource(R.drawable.ic_enterprise)
            "Startup" -> binding.ivCompanyProfileCover.setImageResource(R.drawable.ic_startup)
            else -> binding.ivCompanyProfileCover.setImageResource(R.drawable.ic_software_house)
        }

        // Set Company Photo
        Picasso.get()
                .load("http://54.82.81.23:911/image/$companyImage")
                .resize(512, 512)
                .centerCrop()
                .into(binding.ivCompanyProfileImage)

        binding.ivCompanyLinkedIn.setOnClickListener {
            Toast.makeText(
                    this@CompanyProfileActivity,
                    "Your Linked is: $companyLinkedin",
                    Toast.LENGTH_SHORT
            ).show()
        }

        binding.ivCompanyInstagram.setOnClickListener {
            Toast.makeText(
                    this@CompanyProfileActivity,
                    "Your Instagram is: $companyInstagram",
                    Toast.LENGTH_SHORT
            ).show()
        }

        binding.ivCompanyFacebook.setOnClickListener {
            Toast.makeText(
                    this@CompanyProfileActivity,
                    "Your Facebook is: $companyFacebook",
                    Toast.LENGTH_SHORT
            ).show()
        }


        binding.menuButton.setOnClickListener {
            val showMenu = PopupMenu(this@CompanyProfileActivity, binding.menuButton)
            showMenu.menu.add(Menu.NONE, 0, 0, "Edit Profile")
            showMenu.menu.add(Menu.NONE, 1, 1, "Preview Profile")
            showMenu.show()

            showMenu.setOnMenuItemClickListener { menuItem ->
                val id = menuItem.itemId
                when (id) {
                    0 -> {
                        val intent = Intent(
                                this@CompanyProfileActivity,
                                CompanyEditProfileActivity::class.java
                        )
                        intent.putExtra("companyID", companyID)
                        intent.putExtra("companyName", companyName)
                        startActivity(intent)
                    }
                    1 -> {
                        startActivity(
                                Intent(this@CompanyProfileActivity, HomeActivity::class.java))
                    }
                }
                false
            }
        }

        // End Of Loading
        binding.loadingScreen.visibility = View.GONE

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //RecyclerViewAdapter Manager
        binding.rvCompanyLookingFor.adapter = CompanyLookingForAdapter()
        binding.rvCompanyLookingFor.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        binding.backButton.setOnClickListener {
            finish()
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

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 12f))
        googleMap.setOnMarkerClickListener(this)
        }

        override fun onMarkerClick(marker: Marker): Boolean {
            Toast.makeText(this, marker.title, Toast.LENGTH_SHORT).show()
            return false
        }
}