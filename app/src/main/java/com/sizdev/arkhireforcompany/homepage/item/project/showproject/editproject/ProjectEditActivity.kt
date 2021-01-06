package com.sizdev.arkhireforcompany.homepage.item.project.showproject.editproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.databinding.ActivityProjectEditBinding
import com.sizdev.arkhireforcompany.homepage.HomeActivity
import com.sizdev.arkhireforcompany.homepage.item.project.showproject.ProjectResponse
import com.sizdev.arkhireforcompany.networking.ArkhireApiClient
import com.sizdev.arkhireforcompany.networking.ArkhireApiService
import kotlinx.coroutines.*

class ProjectEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProjectEditBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: ArkhireApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_project_edit)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ArkhireApiClient.getApiClient(this)!!.create(ArkhireApiService::class.java)

        // Get Data
        val projectID = intent.getStringExtra("projectID")
        val projectTitle = intent.getStringExtra("projectTitle")
        val projectDuration = intent.getStringExtra("projectDuration")
        val projectSalary = intent.getStringExtra("projectSalary")
        val projectDesc = intent.getStringExtra("projectDesc")

        binding.btUpdateProjectDone.setOnClickListener {
            if (projectID != null) {
                var newProjectTitle = binding.etProjectTitle.text.toString()
                var newProjectDuration = binding.etProjectDuration.text.toString()
                var newProjectSalary = binding.etProjectSalary.text.toString()
                var newProjectDesc = binding.etProjectDesc.text.toString()

                if (newProjectTitle.isEmpty()){
                    if (projectTitle != null) {
                        newProjectTitle = projectTitle
                    }
                }
                if (newProjectDuration.isEmpty()){
                    if (projectDuration != null) {
                        newProjectDuration = projectDuration
                    }
                }
                if (newProjectSalary.isEmpty()){
                    if (projectSalary != null) {
                        newProjectSalary = projectSalary
                    }
                }
                else{
                    newProjectSalary = "Rp. ${binding.etProjectSalary.text}"
                }

                if (newProjectDesc.isEmpty()){
                    if (projectDesc != null) {
                        newProjectDesc = projectDesc
                    }
                }

                updateProjectData(projectID, newProjectTitle, newProjectDuration, newProjectDesc, newProjectSalary)
            }
        }
    }

    private fun updateProjectData(projectID: String, projectTitle: String, projectDuration: String, projectDesc: String , projectSalary: String ) {
        coroutineScope.launch {

            val result = withContext(Dispatchers.IO) {
                try {
                    service.updateProjectResponse(projectID, projectTitle, projectDuration, projectDesc, projectSalary)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is ProjectResponse) {
                val intent = Intent(this@ProjectEditActivity, HomeActivity::class.java)
                Toast.makeText(
                    this@ProjectEditActivity,
                    "Project Updated Successfully",
                    Toast.LENGTH_LONG
                ).show()
                startActivity(intent)
                finish()
            }
        }
    }
}