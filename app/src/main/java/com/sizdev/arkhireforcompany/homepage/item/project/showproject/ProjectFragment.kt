package com.sizdev.arkhireforcompany.homepage.item.project.showproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.databinding.FragmentProjectBinding
import com.sizdev.arkhireforcompany.homepage.item.project.createproject.CreateProjectActivity
import com.sizdev.arkhireforcompany.networking.ArkhireApiClient
import com.sizdev.arkhireforcompany.networking.ArkhireApiService
import kotlinx.coroutines.*
import okhttp3.internal.notify
import retrofit2.HttpException
import retrofit2.Response

class ProjectFragment : Fragment() {

    private lateinit var binding: FragmentProjectBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: ArkhireApiService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_project, container, false)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = activity?.let { ArkhireApiClient.getApiClient(it) }!!.create(ArkhireApiService::class.java)

        binding.rvProjectList.adapter = ProjectAdapter()
        binding.rvProjectList.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

        // Data Loading Management
        binding.loadingScreen.visibility = View.VISIBLE
        binding.progressBar.max = 100

        // Get Saved ID
        val sharedPrefData = requireActivity().getSharedPreferences("Token", Context.MODE_PRIVATE)
        val savedID = sharedPrefData.getString("accID", null)

        // Data Refresh Management
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object : Runnable {
            override fun run() {
                showAllProjectList(savedID!!)
                mainHandler.postDelayed(this, 2000)
            }
        })

        // Enable FAB
        binding.btCreateProject.setOnClickListener {
            val intent = Intent(activity, CreateProjectActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    private fun showAllProjectList(accountID: String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getAllProjectResponse(accountID)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is ProjectResponse) {
                val list = result.data?.map{
                    ProjectModel(it.projectID, it.projectTitle, it.projectDuration, it.projectDesc, it.projectSalary, it.projectImage, it.postedAt)
                }

                (binding.rvProjectList.adapter as ProjectAdapter).addList(list)

                // End Of Loading
                binding.loadingScreen.visibility = View.GONE
            }

        }
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }

}