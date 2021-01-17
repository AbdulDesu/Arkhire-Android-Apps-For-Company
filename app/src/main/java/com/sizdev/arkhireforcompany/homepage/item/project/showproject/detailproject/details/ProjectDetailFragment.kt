package com.sizdev.arkhireforcompany.homepage.item.project.showproject.detailproject.details

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.databinding.FragmentProjectDetailBinding
import com.sizdev.arkhireforcompany.homepage.item.project.showproject.ProjectResponse
import com.sizdev.arkhireforcompany.homepage.item.project.showproject.editproject.ProjectEditActivity
import com.sizdev.arkhireforcompany.networking.ArkhireApiClient
import com.sizdev.arkhireforcompany.networking.ArkhireApiService
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.alert_delete_project_confirmation.view.*
import kotlinx.coroutines.*

class ProjectDetailFragment : Fragment() {

    private lateinit var binding: FragmentProjectDetailBinding
    private lateinit var dialog: AlertDialog
    private lateinit var service: ArkhireApiService
    private lateinit var coroutineScope: CoroutineScope

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_project_detail, container, false)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = activity?.let { ArkhireApiClient.getApiClient(it) }!!.create(ArkhireApiService::class.java)

        //Get Data
        val projectID = activity?.intent?.getStringExtra("projectID")

        // Data Loading Management
        binding.loadingScreen.visibility = View.VISIBLE
        binding.progressBar.max = 100

        // Data Refresh Management
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object : Runnable {
            override fun run() {
                showProject(projectID!!)
                mainHandler.postDelayed(this, 5000)
            }
        })

        binding.tvProjectDelete.setOnClickListener {
            projectDeleteConfirmation()
            dialog.show()
        }
        return  binding.root
    }

    private fun showProject(projectID: String) {
        coroutineScope.launch {

            val result = withContext(Dispatchers.IO) {
                try {
                    service.getProjectByIDResponse(projectID)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is ProjectDetailResponse) {
                binding.tvProjectTitle.text = result.data.projectTitle
                binding.tvProjectDuration.text = result.data.projectDuration
                binding.tvProjectSalary.text = result.data.projectSalary
                binding.tvProjectDesc.text = result.data.projectDesc
                Picasso.get()
                        .load("http://54.82.81.23:911/image/${result.data.projectImage}")
                        .resize(1280, 560)
                        .centerCrop()
                        .into(binding.ivProjectImage)

                // Enable Edit Button
                binding.tvProjectEdit.setOnClickListener {
                    val intent = Intent(activity, ProjectEditActivity::class.java)
                    intent.putExtra("projectID", result.data.projectID)
                    intent.putExtra("projectTitle", result.data.projectTitle)
                    intent.putExtra("projectDuration", result.data.projectDuration)
                    intent.putExtra("projectSalary", result.data.projectSalary)
                    intent.putExtra("projectDesc", result.data.projectDesc)
                    startActivity(intent)
                }

                // End Of Loading
                binding.loadingScreen.visibility = View.GONE
            }
        }
    }

    private fun projectDeleteConfirmation() {
        val view: View = layoutInflater.inflate(R.layout.alert_delete_project_confirmation, null)
        val projectID = activity?.intent?.getStringExtra("projectID")

        dialog = this.let {
            activity?.let { it1 ->
                AlertDialog.Builder(it1)
                        .setView(view)
                        .setCancelable(false)
                        .create()
            }
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
                        activity,
                        "Project Deleted Successfully",
                        Toast.LENGTH_LONG
                ).show()

                activity?.finish()
            }
        }
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}