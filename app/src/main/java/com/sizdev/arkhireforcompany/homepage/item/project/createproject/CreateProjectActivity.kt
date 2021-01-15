package com.sizdev.arkhireforcompany.homepage.item.project.createproject

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.loader.content.CursorLoader
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.administration.login.LoginActivity
import com.sizdev.arkhireforcompany.databinding.ActivityCreateProjectBinding
import com.sizdev.arkhireforcompany.homepage.HomeActivity
import com.sizdev.arkhireforcompany.homepage.item.account.profile.edit.CompanyEditProfileActivity
import com.sizdev.arkhireforcompany.networking.ArkhireApiClient
import com.sizdev.arkhireforcompany.networking.ArkhireApiService
import kotlinx.android.synthetic.main.alert_logout_confirmation.view.*
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class CreateProjectActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateProjectBinding
    private lateinit var viewModel: CreateProjectViewModel
    private lateinit var dialog: AlertDialog
    private var companyTag: String? = null

    companion object {
        private const val IMAGE_PICK_CODE = 1000;
        const val PERMISSION_CODE = 1001;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_project)
        viewModel = ViewModelProvider(this).get(CreateProjectViewModel::class.java)

        // Get Data
        val sharedPref = this.getSharedPreferences("Token", Context.MODE_PRIVATE)
        val companyID = sharedPref.getString("accCompany", null)

        // Set Data
        companyTag = companyID!!

        val service = ArkhireApiClient.getApiClient(this)?.create(ArkhireApiService::class.java)
        if (service != null) {
            viewModel.setService(service)
        }

        binding.btPickImage.setOnClickListener{
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

        binding.btCreateProject.setOnClickListener {
            Toast.makeText(this, "Please Input All Field !", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        when(requestCode) {
            CreateProjectActivity.PERMISSION_CODE -> {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == CreateProjectActivity.IMAGE_PICK_CODE) {
            binding.btPickImage.visibility = View.GONE
            binding.ivProjectImage.setImageURI(data?.data)

            val filePath = data?.data?.let { getPath(this, it) }
            val file = File(filePath)

            val projectOwner = createPartFromString(companyTag!!)
            val projectTitle = createPartFromString(binding.etProjectName.text.toString())
            val projectDuration = createPartFromString(binding.etProjectDuration.text.toString())
            val projectDesc= createPartFromString(binding.etProjectDesc.text.toString())
            val projectSalary = createPartFromString(binding.etProjectSallary.text.toString())
            var projectImage: MultipartBody.Part? = null
            val mediaTypeImg = "image/jpeg".toMediaType()
            val inputStream = data?.data?.let { contentResolver.openInputStream(it) }
            val reqFile: RequestBody? = inputStream?.readBytes()?.toRequestBody(mediaTypeImg)

            projectImage = reqFile?.let { it1 ->
                MultipartBody.Part.createFormData("project_image", file.name, it1)
            }

            binding.btCreateProject.setOnClickListener {
                if(binding.etProjectName.text.isEmpty() || binding.etProjectDuration.text.isEmpty() || binding.etProjectDesc.text.isEmpty() || binding.etProjectSallary.text.isEmpty()){
                    Toast.makeText(this, "Please Fill Required Field!", Toast.LENGTH_SHORT).show()
                }
                else {
                    if (projectImage != null) {
                        startUploadingAlert()
                        dialog.show()
                        viewModel.createProject(projectTitle, projectDuration, projectDesc, projectSalary, projectOwner, projectImage)
                    }
                }
            }

            subscribeLiveData()
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, CreateProjectActivity.IMAGE_PICK_CODE)
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
                Toast.makeText(this, "Project Created !", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                finish()
            } else {
                Toast.makeText(this, "Failed, To Create Project!", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun startUploadingAlert() {
        val view: View = layoutInflater.inflate(R.layout.alert_uploading_file, null)

        dialog = this.let {
            AlertDialog.Builder(it)
                    .setView(view)
                    .setCancelable(false)
                    .create()
        }

    }

}