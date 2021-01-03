package com.sizdev.arkhireforcompany.homepage.item.project.showproject

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.databinding.FragmentProjectBinding
import com.sizdev.arkhireforcompany.networking.ApiClient
import kotlinx.coroutines.*

class ProjectFragment : Fragment() {

    private lateinit var binding: FragmentProjectBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: ProjectApiService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_project, container, false)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = activity?.let { ApiClient.getApiClient(it) }!!.create(ProjectApiService::class.java)

        binding.rvProjectList.adapter = ProjectAdapter()
        binding.rvProjectList.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

        showAllProjectList()


        return binding.root
    }

    private fun showAllProjectList() {
        coroutineScope.launch {
            Log.d("Arkhire Company", "Start: ${Thread.currentThread().name}")

            val result = withContext(Dispatchers.IO) {
                Log.d("Arkhire Company", "CallApi: ${Thread.currentThread().name}")
                try {
                    service?.getAllProjectResponse()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is ProjectResponse) {
                Log.d("Arkhire Company", result.toString())
                val list = result.data?.map{
                    ProjectModel(it.offeringID, it.projectID, it.projectTitle, it.projectDuration, it.projectDesc, it.projectSalary, it.hiringStatus, it.replyMsg, it.repliedAt)
                }

                (binding.rvProjectList.adapter as ProjectAdapter).addList(list)
            }

        }
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}