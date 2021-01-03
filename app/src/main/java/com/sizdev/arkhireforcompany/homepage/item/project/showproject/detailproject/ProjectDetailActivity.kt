package com.sizdev.arkhireforcompany.homepage.item.project.showproject.detailproject

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.databinding.ActivityProjectDetailBinding
import com.sizdev.arkhireforcompany.homepage.item.project.showproject.editproject.ProjectEditActivity
import com.sizdev.arkhireforcompany.homepage.item.project.showproject.ProjectApiService
import com.sizdev.arkhireforcompany.homepage.item.project.showproject.ProjectResponse
import com.sizdev.arkhireforcompany.networking.ApiClient
import kotlinx.android.synthetic.main.alert_delete_project_confirmation.view.*
import kotlinx.android.synthetic.main.alert_logout_confirmation.view.*
import kotlinx.coroutines.*

class ProjectDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProjectDetailBinding
    private lateinit var dialog: AlertDialog
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: ProjectApiService

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_project_detail)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(this)!!.create(ProjectApiService::class.java)

        //Set Action Bar
        setSupportActionBar(binding.tbDetailsProject)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.tbDetailsProject.setNavigationOnClickListener {
            finish()
        }

        //Get Data
        val projectID = intent.getStringExtra("projectID")
        val projectTitle = intent.getStringExtra("projectTitle")
        val projectDuration = intent.getStringExtra("projectDuration")
        val projectSalary = intent.getStringExtra("projectSalary")
        val projectStatus = intent.getStringExtra("projectStatus")
        val projectDesc = intent.getStringExtra("projectDesc")
        val replyMsg = intent.getStringExtra("msgReply")

        //Set Data
        binding.tvProjecTitle.text = projectTitle
        binding.tvProjectDuration.text = projectDuration
        binding.tvProjectSalary.text = projectSalary
        binding.tvProjectStatus.text = projectStatus
        binding.tvProjectDesc.text = projectDesc

        when (projectStatus){
            "Waiting" -> binding.lnReplyMessage.visibility = View.GONE
            else -> {
                binding.tvProjectReplyMsg.text = replyMsg
                binding.tvProjectEdit.visibility = View.GONE
                binding.tvProjectDelete.visibility = View.GONE
            }
        }

        binding.tvProjectEdit.setOnClickListener {
            val intent = Intent(this, ProjectEditActivity::class.java)
            intent.putExtra("projectID", projectID)
            intent.putExtra("projectTitle", projectTitle)
            intent.putExtra("projectDuration", projectDuration)
            intent.putExtra("projectSalary", projectSalary)
            intent.putExtra("projectDesc", projectDesc)
            startActivity(intent)
        }

        binding.tvProjectDelete.setOnClickListener {
            projectDeleteConfirmation()
            dialog.show()
        }
    }

    private fun projectDeleteConfirmation() {
        val view: View = layoutInflater.inflate(R.layout.alert_delete_project_confirmation, null)
        val projectID = intent.getStringExtra("projectID")

        dialog = this.let {
            AlertDialog.Builder(it)
                .setView(view)
                .setCancelable(false)
                .create()
        }!!

        view.bt_yesDelete.setOnClickListener {
            if (projectID != null) {
                deleteProject(projectID)
            }
            dialog.dismiss()
        }

        view.bt_noDelete.setOnClickListener {
            dialog.cancel()
        }
    }

    private fun deleteProject(projectID : String) {
        coroutineScope.launch {

            val result = withContext(Dispatchers.IO) {
                try {
                    service.deleteProjectResponse(projectID)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is ProjectResponse) {
                Toast.makeText(
                    this@ProjectDetailActivity,
                    "Project Deleted Successfully",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
        }
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}