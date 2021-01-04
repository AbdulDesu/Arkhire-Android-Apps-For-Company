package com.sizdev.arkhireforcompany.homepage.item.project.createproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.administration.login.LoginActivity
import com.sizdev.arkhireforcompany.administration.register.RegisterAuthService
import com.sizdev.arkhireforcompany.administration.register.RegisterResponse
import com.sizdev.arkhireforcompany.databinding.ActivityCreateProjectBinding
import com.sizdev.arkhireforcompany.homepage.HomeActivity
import com.sizdev.arkhireforcompany.homepage.item.project.showproject.ProjectApiService
import com.sizdev.arkhireforcompany.networking.ApiClient
import kotlinx.coroutines.*

class CreateProjectActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateProjectBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: ProjectApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_project)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(this)!!.create(ProjectApiService::class.java)

        binding.btSendOffering.setOnClickListener {
            val projectName = binding.etProjectName.text.toString()
            val projectSalary = binding.etProjectSallary.text.toString()
            val projectDuration = binding.etProjectDuration.text.toString()
            val projectDesc = binding.etProjectDesc.text.toString()

            sendOfferingMessage(projectName,projectDuration, projectDesc, "Rp. $projectSalary")
        }
    }

    private fun sendOfferingMessage(projectName:String, projectDuration:String, projectDesc:String, projectSalary:String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.createProjectRequest(projectName, projectDuration, projectDesc, projectSalary)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (result is CreateProjectResponse) {
                Toast.makeText(this@CreateProjectActivity, "Project Created", Toast.LENGTH_LONG).show()
                val intent = Intent(this@CreateProjectActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}