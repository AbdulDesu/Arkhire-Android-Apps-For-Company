package com.sizdev.arkhireforcompany.homepage.item.account.profile


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import com.sizdev.arkhireforcompany.homepage.item.account.AccountApiService
import com.sizdev.arkhireforcompany.homepage.item.account.AccountResponse
import com.sizdev.arkhireforcompany.networking.ApiClient
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*

class CompanyProfileActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var binding: ActivityCompanyProfileBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: AccountApiService
    private lateinit var markerDefault: Marker

    private var defaultLocation = LatLng(-6.200000, 106.816666)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_company_profile)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(this)!!.create(AccountApiService::class.java)

        val sharedPrefData = this.getSharedPreferences("Token", Context.MODE_PRIVATE)
        val accountOwner = sharedPrefData.getString("accName", null)
        val companyLatitude = intent.getStringExtra("companyLatitude")
        val companyLongitude = intent.getStringExtra("companyLongitude")

        // Set Map
        if (companyLatitude != "null" && companyLongitude != "null") {
            if (companyLatitude != null && companyLongitude != null) {
                defaultLocation = LatLng(companyLatitude.toDouble(), companyLongitude.toDouble())
            }
        }


        //Show Company Data
        if (accountOwner != null) {
            showCompanyProfile(accountOwner)
        }

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

    private fun showCompanyProfile(accountOwner: String) {
        coroutineScope.launch {
            Log.d("Arkhire Company", "Start: ${Thread.currentThread().name}")

            val result = withContext(Dispatchers.IO) {
                Log.d("Arkhire Company", "CallApi: ${Thread.currentThread().name}")
                try {
                    service?.getAccountDataByNameResponse(accountOwner)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is AccountResponse) {
                binding.tvCompanyProfileName.text = result.data[0].companyName
                binding.tvCompanyType.text = result.data[0].companyType
                binding.tvCompanyDescription.text = result.data[0].companyDesc

                // Set Background
                when (result.data[0].companyType) {
                    "Enterprise" -> binding.ivCompanyProfileCover.setImageResource(R.drawable.ic_enterprise)
                    "Startup" -> binding.ivCompanyProfileCover.setImageResource(R.drawable.ic_startup)
                    else -> binding.ivCompanyProfileCover.setImageResource(R.drawable.ic_software_house)
                }

                // Set Company Photo
                Picasso.get()
                    .load("http://54.82.81.23:911/image/${result.data[0].companyImage}")
                    .resize(512, 512)
                    .centerCrop()
                    .into(binding.ivCompanyProfileImage)

                binding.ivCompanyLinkedIn.setOnClickListener {
                    Toast.makeText(
                        this@CompanyProfileActivity,
                        "Your Linked is: ${result.data[0].companyLinkedin}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                binding.ivCompanyInstagram.setOnClickListener {
                    Toast.makeText(
                        this@CompanyProfileActivity,
                        "Your Instagram is: ${result.data[0].companyInstagram}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                binding.ivCompanyFacebook.setOnClickListener {
                    Toast.makeText(
                        this@CompanyProfileActivity,
                        "Your Facebook is: ${result.data[0].companyFacebook}",
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
                                intent.putExtra("companyID", result.data[0].companyID)
                                intent.putExtra("companyName", result.data[0].companyName)
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
            }
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

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }

}