package com.sizdev.arkhireforcompany.homepage.item.account.profile.edit

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.loader.content.CursorLoader
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.databinding.ActivityCompanyEditProfileBinding
import com.sizdev.arkhireforcompany.networking.ArkhireApiClient
import com.sizdev.arkhireforcompany.networking.ArkhireApiService
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_company_edit_profile.*
import kotlinx.android.synthetic.main.activity_company_profile.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.*


class CompanyEditProfileActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var binding: ActivityCompanyEditProfileBinding
    private lateinit var viewModel: CompanyEditProfileViewModel
    private lateinit var markerDefault: Marker

    private var locationDefault = LatLng(-6.200000, 106.816666)
    private var companyID: String? =null

    companion object {
        private const val IMAGE_PICK_CODE = 1000
        const val PERMISSION_CODE = 1001
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_company_edit_profile)

        // Set Service
        setService()

        // Subscribe Live Data
        subscribeLiveData()

        // Set Map
        setMap()

        // Get Current Login Company Data
        setCurrentData()

        // Set Pop Up
        setPopUp()

        val editCode = intent.getStringExtra("editCode")

        if(editCode == "0"){
            binding.btNewProfileDone.setOnClickListener {
                Toast.makeText(this, "Please Fill All Required Field !", Toast.LENGTH_SHORT).show()
            }
        }

        else{
            binding.btNewProfileDone.setOnClickListener {
                val currentImage = intent.getStringExtra("companyImage")
                if(binding.etCompanyLocation.text.isEmpty() || binding.tvCurrentLatitude.text.isEmpty() || binding.tvCurrentLongitude.text.isEmpty() || binding.etEditCompanyType.text.isEmpty() || binding.etEditCompanyDesc.text.isEmpty()){
                    Toast.makeText(this, "Please Fill All Field", Toast.LENGTH_LONG).show()
                }
                else {
                    viewModel.updateCompanyWithoutImage(
                            companyID!!,
                            binding.etCompanyLocation.text.toString(),
                            binding.tvCurrentLatitude.text.toString(),
                            binding.tvCurrentLongitude.text.toString(),
                            binding.etEditCompanyType.text.toString(),
                            binding.etEditCompanyDesc.text.toString(),
                            binding.etEditCompanyLinkedin.text.toString(),
                            binding.etEditCompanyInstagram.text.toString(),
                            binding.etEditCompanyFacebook.text.toString(),
                            currentImage!!)
                }
            }
        }
        binding.btEditProfileImage.setOnClickListener {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED){
                //permission denied
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                //show popup to request runtime permission
                requestPermissions(permissions, CompanyEditProfileActivity.PERMISSION_CODE);
            }
            else{
                //permission already granted
                if(binding.etCompanyLocation.text.isNullOrEmpty() || binding.etEditCompanyType.text.isNullOrEmpty() || binding.etEditCompanyDesc.text.isNullOrEmpty() || binding.tvCurrentLatitude.text.isNullOrEmpty() || binding.tvCurrentLongitude.text.isNullOrEmpty()){
                    Toast.makeText(this, "Please Input All Required field, Include set location !", Toast.LENGTH_SHORT).show()
                }
                else {
                    pickImageFromGallery();
                }

            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setPopUp() {
        //Company Type Pop Up Menu
        val companyType = PopupMenu(this, binding.etEditCompanyType)

        companyType.menu.add(Menu.NONE, 0 ,0, "Enterprise")
        companyType.menu.add(Menu.NONE, 1 ,1, "Startup")
        companyType.menu.add(Menu.NONE, 2 ,2, "Software House")

        //Company Type Listener
        companyType.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                0 -> {binding.etEditCompanyType.text ="Enterprise"}
                1 -> {binding.etEditCompanyType.text ="Startup"}
                2 -> {binding.etEditCompanyType.text ="Software House"}
            }
            false
        }

        //Show Pop Up when clicked
        binding.etEditCompanyType.setOnClickListener {
            companyType.show()
        }
    }

    private fun setCurrentData() {
        companyID = intent.getStringExtra("companyID")
        binding.etCompanyLocation.setText(intent.getStringExtra("companyLocation"))
        binding.etEditCompanyType.setText(intent.getStringExtra("companyType"))
        binding.etEditCompanyDesc.setText(intent.getStringExtra("companyDesc"))
        binding.etEditCompanyLinkedin.setText(intent.getStringExtra("companyLinkedin"))
        binding.etEditCompanyInstagram.setText(intent.getStringExtra("companyInstagram"))
        binding.etEditCompanyFacebook.setText(intent.getStringExtra("companyFacebook"))
        binding.tvCurrentLatitude.text = intent.getStringExtra("companyLatitude")
        binding.tvCurrentLongitude.text = intent.getStringExtra("companyLongitude")

        when(intent.getStringExtra("companyImage")){
            null -> binding.ivEditProfileImage.setImageResource(R.drawable.ic_empty_image)
            else -> {
                Picasso.get()
                        .load("http://54.82.81.23:911/image/${intent.getStringExtra("companyImage")}")
                        .resize(512, 512)
                        .centerCrop()
                        .into(binding.ivEditProfileImage)
            }
        }
    }

    private fun setMap() {
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.editCompanyLocationmap) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setService() {
        viewModel = ViewModelProvider(this).get(CompanyEditProfileViewModel::class.java)
        val service = ArkhireApiClient.getApiClient(this)?.create(ArkhireApiService::class.java)
        viewModel.setService(service!!)
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        when(requestCode) {
            CompanyEditProfileActivity.PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    //permission from popup granted
                    if(binding.etCompanyLocation.text.isNullOrEmpty() || binding.etEditCompanyType.text.isNullOrEmpty() || binding.etEditCompanyDesc.text.isNullOrEmpty() || binding.tvCurrentLatitude.text.isNullOrEmpty() || binding.tvCurrentLongitude.text.isNullOrEmpty()){
                        Toast.makeText(this, "Please Input All Required field, Include set location !", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        pickImageFromGallery()
                    }

                } else {
                    //permission from popup denied
                    Toast.makeText(this, "Please Allow Permission", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, CompanyEditProfileActivity.IMAGE_PICK_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == CompanyEditProfileActivity.IMAGE_PICK_CODE) {
            binding.ivEditProfileImage.setImageURI(data?.data)

            val filePath = data?.data?.let { getPath(this, it) }
            val file = File(filePath)

            val companyLocation = createPartFromString(binding.etCompanyLocation.text.toString())
            val companyLatitude = createPartFromString(binding.tvCurrentLatitude.text.toString())
            val companyLongitude = createPartFromString(binding.tvCurrentLongitude.text.toString())
            val companyType = createPartFromString(binding.etEditCompanyType.text.toString())
            val companyDesc = createPartFromString(binding.etEditCompanyDesc.text.toString())
            val companyLinkedin = createPartFromString(binding.etEditCompanyLinkedin.text.toString())
            val companyInstagram = createPartFromString(binding.etEditCompanyInstagram.text.toString())
            val companyFacebook = createPartFromString(binding.etEditCompanyFacebook.text.toString())

            var companyImage: MultipartBody.Part? = null
            val mediaTypeImg = "image/jpeg".toMediaType()
            val inputStream = data?.data?.let { contentResolver.openInputStream(it) }
            val reqFile: RequestBody? = inputStream?.readBytes()?.toRequestBody(mediaTypeImg)

            companyImage = reqFile?.let { it1 ->
                MultipartBody.Part.createFormData("company_image", file.name, it1)
            }

            binding.btNewProfileDone.setOnClickListener {
                if(binding.etEditCompanyType.text.isEmpty() || binding.etEditCompanyDesc.text.isEmpty()){
                    Toast.makeText(this, "Please Fill Required Field!", Toast.LENGTH_SHORT).show()
                }
                else {
                    if (companyImage != null) {
                        viewModel.updateCompany(companyID!!, companyLocation, companyLatitude, companyLongitude, companyType, companyDesc, companyLinkedin, companyInstagram, companyFacebook, companyImage)
                    }
                    else {
                        Toast.makeText(this, "Please Pick Image!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isMyLocationButtonEnabled = true
        googleMap.mapType = GoogleMap.MAP_TYPE_TERRAIN

        googleMap.setOnCameraMoveStartedListener {
            binding.editCompanyLocationmap.parent.requestDisallowInterceptTouchEvent(true)

        }

        googleMap.setOnCameraIdleListener {
            binding.editCompanyLocationmap.parent.requestDisallowInterceptTouchEvent(false)
        }

        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.isMyLocationEnabled = true
            googleMap.setOnMyLocationClickListener (object : GoogleMap.OnMyLocationClickListener {
                override fun onMyLocationClick(p0: Location) {
                    binding.tvCurrentLatitude.text = p0.latitude.toString()
                    binding.tvCurrentLongitude.text = p0.longitude.toString()
                    markerDefault.position = LatLng(p0.latitude, p0.longitude)
                }
            })
        }

        markerDefault = googleMap.addMarker(
                MarkerOptions()
                        .position(locationDefault)
                        .draggable(true)
        )
        val companyLatitude = intent.getStringExtra("companyLatitude")
        val companyLongitude = intent.getStringExtra("companyLongitude")

        if(companyLongitude == null || companyLatitude == null){
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationDefault, 16f))
            googleMap.setOnMarkerClickListener(this)
        }

        else if(companyLongitude != "" && companyLatitude != ""){
            markerDefault.position = LatLng(companyLatitude!!.toDouble(), companyLongitude!!.toDouble())
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(companyLatitude.toDouble(), companyLongitude.toDouble()), 16f))
            googleMap.setOnMarkerClickListener(this)
        }

        else {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationDefault, 16f))
            googleMap.setOnMarkerClickListener(this)
        }


        googleMap.setOnMapClickListener(object : GoogleMap.OnMapClickListener {
            override fun onMapClick(p0: LatLng) {
                binding.tvCurrentLatitude.text = p0.latitude.toString()
                binding.tvCurrentLongitude.text = p0.longitude.toString()
                markerDefault.position = p0
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(p0, 16f))
                googleMap.setOnMarkerClickListener(this@CompanyEditProfileActivity)
            }
        })

    }


    override fun onMarkerClick(marker: Marker?): Boolean {
        Toast.makeText(this, "Select Area On Map To Update Location", Toast.LENGTH_SHORT).show()
        return false
    }

    private fun getPath(context: Context, contentUri: Uri) : String? {
        var result: String? = null
        val data = arrayOf(MediaStore.Images.Media.DATA)

        val cursorLoader = CursorLoader(context, contentUri, data, null, null, null)
        val cursor = cursorLoader.loadInBackground()

        if (cursor != null) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            result = cursor.getString(columnIndex)
            cursor.close()
        }
        return result
    }

    @NonNull
    private fun createPartFromString(json: String): RequestBody {
        val mediaType = "multipart/form-data".toMediaType()
        return json
            .toRequestBody(mediaType)
    }

    private fun subscribeLiveData() {
        viewModel.isLoading.observe(this, {
            binding.loadingScreen.visibility = View.VISIBLE
        })

        viewModel.isSuccess.observe(this, {
            if (viewModel.isSuccess.value == "Success") {
                Toast.makeText(this, "Profile Updated !", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed, To Update !", Toast.LENGTH_LONG).show()
            }
        })
    }
}