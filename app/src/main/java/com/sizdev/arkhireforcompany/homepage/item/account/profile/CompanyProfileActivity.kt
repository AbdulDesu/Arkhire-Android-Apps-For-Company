package com.sizdev.arkhireforcompany.homepage.item.account.profile

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
import com.sizdev.arkhireforcompany.administration.login.LoginActivity
import com.sizdev.arkhireforcompany.databinding.ActivityCompanyProfileBinding
import com.sizdev.arkhireforcompany.homepage.HomeActivity
import com.sizdev.arkhireforcompany.homepage.item.account.AccountPresenter
import com.sizdev.arkhireforcompany.homepage.item.account.profile.edit.CompanyEditProfileActivity
import com.sizdev.arkhireforcompany.networking.ArkhireApiClient
import com.sizdev.arkhireforcompany.networking.ArkhireApiService
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_company_edit_profile.*
import kotlinx.android.synthetic.main.activity_company_profile.*
import kotlinx.android.synthetic.main.alert_session_expired.view.*
import kotlinx.coroutines.*
import java.lang.Runnable

class CompanyProfileActivity : AppCompatActivity(), CompanyProfileContract.View, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var binding: ActivityCompanyProfileBinding
    private lateinit var markerDefault: Marker
    private lateinit var dialog: AlertDialog
    private lateinit var handler: Handler
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var map: GoogleMap

    private var companyID: String? = null
    private var presenter: CompanyProfilePresenter? = null
    private var defaultLocation = LatLng(-6.200000, 106.816666)
    private var markerName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_company_profile)

        // Set Services
        setService()

        // Show Progress bar
        showProgressBar()

        // Get Current Login Data
        getCurrentLoginData()

        // Set Data Refresh manager
        setDataRefreshManagement()

        // Set Map
        setMap()


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
                .title(markerName)
        )

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 12f))
        googleMap.setOnMarkerClickListener(this)

        map = googleMap

        googleMap.setOnCameraMoveStartedListener {
            binding.map.parent.requestDisallowInterceptTouchEvent(true)

        }

        googleMap.setOnCameraIdleListener {
            binding.map.parent.requestDisallowInterceptTouchEvent(false)
        }

    }

        override fun onMarkerClick(marker: Marker): Boolean {
            Toast.makeText(this, "$markerName Location", Toast.LENGTH_SHORT).show()
            return false
        }

    override fun setError(error: String) {
        when (error){
            "Session Expired !" -> {
                handler.removeCallbacksAndMessages(null)
                showSessionExpiredAlert()
                dialog.show()
            }
            else -> {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun setService() {
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        val service = this.let { ArkhireApiClient.getApiClient(it)?.create(ArkhireApiService::class.java) }
        presenter = CompanyProfilePresenter(coroutineScope, service)
    }

    override fun getCurrentLoginData() {
        val sharedPrefData = this.getSharedPreferences("Token", Context.MODE_PRIVATE)
        companyID = sharedPrefData.getString("accID", null)
    }

    override fun setMap() {
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun setCompanyData(companyID: String, accountID: String, accountName: String, companyName: String, companyLocation: String, companyPosition: String, companyLatitude: String, companyLongitude: String, companyType: String, companyDesc: String, companyLinkedin: String, companyInstagram: String, companyFacebook: String, companyImage: String) {
        markerName = companyName
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

        when(companyLinkedin){
            null -> binding.ivCompanyLinkedIn.setImageResource(R.drawable.ic_linkedin_disabled)
            "" -> binding.ivCompanyLinkedIn.setImageResource(R.drawable.ic_linkedin_disabled)
            else -> {
                binding.ivCompanyLinkedIn.setOnClickListener {
                    Toast.makeText(
                        this@CompanyProfileActivity,
                        "Your Linked is: $companyLinkedin",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        when(companyInstagram){
            null -> binding.ivCompanyInstagram.setImageResource(R.drawable.ic_instagram_disabled)
            "" -> binding.ivCompanyInstagram.setImageResource(R.drawable.ic_instagram_disabled)
            else -> {
                binding.ivCompanyInstagram.setOnClickListener {
                    Toast.makeText(
                        this@CompanyProfileActivity,
                        "Your Instagram is: $companyLinkedin",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        when(companyFacebook){
            null -> binding.ivCompanyFacebook.setImageResource(R.drawable.ic_facebook_disabled)
            "" -> binding.ivCompanyFacebook.setImageResource(R.drawable.ic_facebook_disabled)
            else -> {
                binding.ivCompanyFacebook.setOnClickListener {
                    Toast.makeText(
                        this@CompanyProfileActivity,
                        "Your Facebook is: $companyLinkedin",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
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
                        intent.putExtra("editCode", "1")
                        intent.putExtra("companyID", companyID)
                        intent.putExtra("companyLocation", companyLocation)
                        intent.putExtra("companyLatitude", companyLatitude)
                        intent.putExtra("companyLongitude", companyLongitude)
                        intent.putExtra("companyType", companyType)
                        intent.putExtra("companyDesc", companyDesc)
                        intent.putExtra("companyLinkedin", companyLinkedin)
                        intent.putExtra("companyInstagram", companyInstagram)
                        intent.putExtra("companyFacebook", companyFacebook)
                        intent.putExtra("companyImage", companyImage)
                        startActivity(intent)
                    }
                    1 -> {
                        Toast.makeText(this, "Coming Soon", Toast.LENGTH_LONG).show()
                    }
                }
                false
            }
        }

        if (companyLatitude != "" && companyLongitude != ""){
            markerDefault.position = LatLng(companyLatitude.toDouble(), companyLongitude.toDouble())
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(companyLatitude.toDouble(), companyLongitude.toDouble()), 12f))
            map.setOnMarkerClickListener(this)
        }

        binding.ivHelpLookingFor.setOnClickListener {
            Toast.makeText(this, "Talent/Engineer criteria You searched for any project", Toast.LENGTH_SHORT).show()
        }
        binding.ivHelpCompanyDescription.setOnClickListener {
            Toast.makeText(this, "Describe/Short Information of this company", Toast.LENGTH_SHORT).show()
        }
        binding.ivHelpCompanyLocation.setOnClickListener {
            Toast.makeText(this, "Your Accurate company location, Talent can navigate to this location", Toast.LENGTH_SHORT).show()
        }
    }

    override fun setDataRefreshManagement() {
        handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                presenter?.getCompanyData(companyID!!)
                handler.postDelayed(this, 5000)
            }
        })
    }

    override fun showProgressBar() {
        binding.loadingScreen.visibility = View.VISIBLE
        binding.progressBar.max = 100
    }

    override fun hideProgressBar() {
        binding.loadingScreen.visibility = View.GONE
    }

    override fun showSessionExpiredAlert() {
        val view: View = layoutInflater.inflate(R.layout.alert_session_expired, null)

        dialog = this.let {
            AlertDialog.Builder(it)
                    .setView(view)
                    .setCancelable(false)
                    .create()
        }

        view.bt_okRelog.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this, LoginActivity::class.java)
            val sharedPref = this.getSharedPreferences("Token", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString("accID", null)
            editor.apply()
            startActivity(intent)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        presenter?.bindToView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter?.unbind()
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}