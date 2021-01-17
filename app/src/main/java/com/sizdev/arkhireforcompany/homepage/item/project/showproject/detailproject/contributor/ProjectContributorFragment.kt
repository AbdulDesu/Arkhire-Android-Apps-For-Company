package com.sizdev.arkhireforcompany.homepage.item.project.showproject.detailproject.contributor

import android.content.Context
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.administration.login.LoginActivity
import com.sizdev.arkhireforcompany.databinding.FragmentProjectContributorBinding
import com.sizdev.arkhireforcompany.networking.ArkhireApiClient
import com.sizdev.arkhireforcompany.networking.ArkhireApiService
import kotlinx.android.synthetic.main.alert_session_expired.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

class ProjectContributorFragment : Fragment(), ProjectContributorContract.View {

    private lateinit var binding: FragmentProjectContributorBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var handler: Handler
    private lateinit var dialog: AlertDialog

    private var presenter: ProjectContributorPresenter? = null
    private var projectTag: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_project_contributor, container, false)

        // Set Service
        setService()

        // Get Current Project Data
        getCurrentProjectData()

        // Show Progress Bar
        showProgressBar()

        // Set Data Refresh Management
        dataRefreshManagement()

        // Set Up RecyclerView
        setUpRecyclerView()

        return binding.root

    }

    override fun setService() {
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        val service = activity?.let { ArkhireApiClient.getApiClient(it)?.create(ArkhireApiService::class.java) }
        presenter = ProjectContributorPresenter(coroutineScope, service)
    }

    override fun addListContributor(list: List<ProjectContributorModel>) {
        (binding.rvContributorList.adapter as ProjectContributorAdapter).addList(list)
    }

    override fun setUpRecyclerView() {
        binding.rvContributorList.adapter = ProjectContributorAdapter()
        binding.rvContributorList.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
    }

    override fun setError(error: String) {
        when (error){
            "Session Expired !" -> {
                showSessionExpiredAlert()
                dialog.show()
            }

            "Data Not Found !" -> {
                binding.rvContributorList.visibility = View.GONE
                binding.notfound.visibility = View.VISIBLE
            }

            else -> {
                Toast.makeText(activity, error, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun showProgressBar() {
        binding.loadingScreen.visibility = View.VISIBLE
        binding.progressBar.max = 100
    }

    override fun hideProgressBar() {
        binding.loadingScreen.visibility = View.GONE
    }

    override fun getCurrentProjectData() {
        val projectID = activity?.intent?.getStringExtra("projectID")
        projectTag = projectID
    }

    override fun showSessionExpiredAlert() {
        val view: View = layoutInflater.inflate(R.layout.alert_session_expired, null)

        dialog = activity?.let {
            AlertDialog.Builder(it)
                    .setView(view)
                    .setCancelable(false)
                    .create()
        }!!

        view.bt_okRelog.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(activity, LoginActivity::class.java)
            val sharedPref = requireActivity().getSharedPreferences("Token", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString("accID", null)
            editor.apply()
            startActivity(intent)
            activity?.finish()
        }
    }

    override fun dataRefreshManagement() {
        handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                presenter?.showContributorApi(projectTag!!)
                handler.postDelayed(this, 2000)
            }
        })
    }

    override fun onStart() {
        super.onStart()
        presenter?.bindToView(this)
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