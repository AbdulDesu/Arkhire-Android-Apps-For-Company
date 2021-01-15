package com.sizdev.arkhireforcompany.homepage.item.project.showproject.detailproject.hiringlist

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.databinding.FragmentProjectHiringBinding
import com.sizdev.arkhireforcompany.networking.ArkhireApiClient
import com.sizdev.arkhireforcompany.networking.ArkhireApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

class ProjectHiringFragment : Fragment(), ProjectHiringContract.View {

    private lateinit var binding: FragmentProjectHiringBinding
    private lateinit var coroutineScope: CoroutineScope
    private var presenter: ProjectHiringPresenter?= null
    private var projectTag: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_project_hiring, container, false)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        val service = activity?.let { ArkhireApiClient.getApiClient(it)?.create(ArkhireApiService::class.java) }
        presenter = ProjectHiringPresenter(coroutineScope, service)

        // Set Up SavedData
        val projectID = activity?.intent?.getStringExtra("projectID")
        projectTag = projectID

        // Set Up RecyclerView
        binding.rvHiringList.adapter = ProjectHiringAdapter()
        binding.rvHiringList.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

        return binding.root
    }

    override fun addListHiring(list: List<ProjectHiringModel>) {
        (binding.rvHiringList.adapter as ProjectHiringAdapter).addList(list)
    }

    override fun onStart() {
        super.onStart()
        presenter?.bindToView(this)

        // Data Refresh Management
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object : Runnable {
            override fun run() {
                presenter?.showHiringList(projectTag!!)
                mainHandler.postDelayed(this, 5000)
            }
        })
    }

    override fun onStop() {
        presenter?.unbind()
        super.onStop()
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        presenter = null
        super.onDestroy()
    }

}