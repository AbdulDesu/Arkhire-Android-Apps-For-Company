package com.sizdev.arkhireforcompany.homepage.item.account.profile.edit

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.loader.content.CursorLoader
import com.google.android.gms.location.LocationListener
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.databinding.ActivityCompanyEditProfileBinding
import com.sizdev.arkhireforcompany.homepage.item.account.profile.CompanyProfileActivity
import com.sizdev.arkhireforcompany.networking.ArkhireApiClient
import com.sizdev.arkhireforcompany.networking.ArkhireApiService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.*

class CompanyEditProfileActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener, GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnCameraIdleListener, GoogleMap.OnMarkerClickListener {

    private lateinit var binding: ActivityCompanyEditProfileBinding
    private lateinit var viewModel: CompanyEditProfileViewModel
    private lateinit var markerDefault: Marker
    lateinit var locationManager: LocationManager
    private var locationGps: Location? = null
    private var locationDefault = LatLng(-6.200000, 106.816666)

    companion object {
        private const val IMAGE_PICK_CODE = 1000;
        const val PERMISSION_CODE = 1001;
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_company_edit_profile)
        viewModel = ViewModelProvider(this).get(CompanyEditProfileViewModel::class.java)

        val service = ArkhireApiClient.getApiClient(this)?.create(ArkhireApiService::class.java)
        if (service != null) {
            viewModel.setService(service)
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.editCompanyLocationmap) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.etEditCompanyLocation.setOnClickListener {
            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@setOnClickListener
            }

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0F
            ) { location ->
                locationGps = location
                val latitude = locationGps!!.latitude.toString()
                val longitude = locationGps!!.longitude.toString()

                binding.tvCurrentLatitude.text = latitude
                binding.tvCurrentLongitude.text = longitude
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
                pickImageFromGallery();
            }
        }

        binding.btNewProfileDone.setOnClickListener {

        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            CompanyEditProfileActivity.PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    pickImageFromGallery()
                }
                else{
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

            var companyID = intent.getStringExtra("companyID")

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
                }
            }
            subscribeLiveData()
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

    fun getPath(context: Context, contentUri: Uri) : String? {
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