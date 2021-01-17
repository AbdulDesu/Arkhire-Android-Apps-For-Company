package com.sizdev.arkhireforcompany.homepage.item.project.showproject.detailproject.hiringlist

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
import com.sizdev.arkhireforcompany.databinding.FragmentProjectHiringBinding
import com.sizdev.arkhireforcompany.networking.ArkhireApiClient
import com.sizdev.arkhireforcompany.networking.ArkhireApiService
import kotlinx.android.synthetic.main.alert_session_expired.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

class ProjectHiringFragment : Fragment(), ProjectHiringContract.View {

    private lateinit var binding: FragmentProjectHiringBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var dialog: AlertDialog
    private lateinit var handler: Handler

    private var presenter: ProjectHiringPresenter?= null
    private var projectTag: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_project_hiring, container, false)

        // Set Service
        setService()

        // Get Current Project Data
        getCurrentProjectData()

        // Set Up RecyclerView
        setUpRecyclerView()

        // Show Progress Bar
        showProgressBar()

        // Set Up Data Refresh Management
        dataRefreshManagement()

        return binding.root
    }

    override fun addListHiring(list: List<ProjectHiringModel>) {
        (binding.rvHiringList.adapter as ProjectHiringAdapter).addList(list)
    }

    override fun setService() {
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        val service = activity?.let { ArkhireApiClient.getApiClient(it)?.create(ArkhireApiService::class.java) }
        presenter = ProjectHiringPresenter(coroutineScope, service)
    }

    override fun setError(error: String) {
        when (error){
            "Session Expired !" -> {
                sessionExpiredAlert()
                dialog.show()
            }

            "Data Not Found !" -> {
                binding.rvHiringList.visibility = View.GONE
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

    override fun getCurrentProjectData() {
        projectTag = activity?.intent?.getStringExtra("projectID")
    }

    override fun dataRefreshManagement() {
        handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                presenter?.showHiringList(projectTag!!)
                handler.postDelayed(this, 3000)
            }
        })
    }

    override fun setUpRecyclerView() {
        binding.rvHiringList.adapter = ProjectHiringAdapter()
        binding.rvHiringList.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
    }

    override fun hideProgressBar() {
        binding.loadingScreen.visibility = View.GONE
    }

    override fun sessionExpiredAlert() {
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

    override fun onStart() {
        super.onStart()
        presenter?.bindToView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter?.unbind()
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }

}