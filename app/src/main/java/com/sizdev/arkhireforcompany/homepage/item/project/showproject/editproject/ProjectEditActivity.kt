package com.sizdev.arkhireforcompany.homepage.item.project.showproject.editproject

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.databinding.ActivityProjectEditBinding
import com.sizdev.arkhireforcompany.homepage.HomeActivity
import com.sizdev.arkhireforcompany.homepage.item.project.showproject.ProjectResponse
import com.sizdev.arkhireforcompany.networking.ArkhireApiClient
import com.sizdev.arkhireforcompany.networking.ArkhireApiService
import kotlinx.coroutines.*
import java.util.*

class ProjectEditActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private lateinit var binding: ActivityProjectEditBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: ArkhireApiService

    private var day = 0
    private var month = 0
    private var year = 0

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

        // Set Data
        binding.etProjectTitle.setText(projectTitle)
        binding.etProjectDuration.text = projectDuration
        binding.etProjectSalary.setText(projectSalary)
        binding.etProjectDesc.setText(projectDesc)

        // Set Date Picker
        pickDate()

        binding.btUpdateProjectDone.setOnClickListener {
            if(binding.etProjectTitle.text.isNullOrEmpty() || binding.etProjectSalary.text.isNullOrEmpty() || binding.etProjectDuration.text.isNullOrEmpty()){
                Toast.makeText(this, "Please Input All Required Field!", Toast.LENGTH_SHORT).show()
            }
            else {
                updateProjectData(projectID!!, binding.etProjectTitle.text.toString(), binding.etProjectDuration.text.toString(), binding.etProjectDesc.text.toString(), binding.etProjectSalary.text.toString())
            }
        }
    }

    private fun pickDate() {
        binding.etProjectDuration.setOnClickListener {
            getDateCalendar()

            DatePickerDialog(this, this, year, month, day).show()
        }
    }

    private fun getDateCalendar() {
        val cal: Calendar = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
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

    @SuppressLint("SetTextI18n")
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        getDateCalendar()
        binding.etProjectDuration.text = "$dayOfMonth-${month+1}-$year"
    }
}