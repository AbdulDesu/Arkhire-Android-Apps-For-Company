package com.sizdev.arkhireforcompany.homepage.item.project.showproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.administration.login.LoginActivity
import com.sizdev.arkhireforcompany.databinding.FragmentProjectBinding
import com.sizdev.arkhireforcompany.homepage.item.project.createproject.CreateProjectActivity
import com.sizdev.arkhireforcompany.networking.ArkhireApiClient
import com.sizdev.arkhireforcompany.networking.ArkhireApiService
import kotlinx.android.synthetic.main.alert_session_expired.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

class ProjectFragment : Fragment(), ProjectContract.View {

    private lateinit var binding: FragmentProjectBinding
    private lateinit var dialog: AlertDialog
    private lateinit var handler: Handler
    private lateinit var coroutineScope: CoroutineScope

    private var accountID: String? = null
    private var presenter: ProjectPresenter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_project, container, false)

        // Set Service
        setService()

        // Set Up RecyclerView
        setRecyclerView()

        // Show Progressbar
        showProgressBar()

        // Get Current Login Data
        getCurrentAccount()

        // Data Refresh Management
        dataRefreshManager()

        // Enable FAB
        enableFab()

        return binding.root
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

    private fun getCurrentAccount() {
        val sharedPrefData = requireActivity().getSharedPreferences("Token", Context.MODE_PRIVATE)
        accountID  = sharedPrefData.getString("accID", null)
    }

    private fun showProgressBar() {
        binding.loadingScreen.visibility = View.VISIBLE
        binding.progressBar.max = 100
    }

    private fun setRecyclerView() {
        binding.rvProjectList.adapter = ProjectAdapter()
        binding.rvProjectList.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
    }

    private fun setService() {
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        val service = activity?.let { ArkhireApiClient.getApiClient(it)?.create(ArkhireApiService::class.java) }
        presenter = ProjectPresenter(coroutineScope, service)
    }

    private fun enableFab() {
        binding.btCreateProject.setOnClickListener {
            val intent = Intent(activity, CreateProjectActivity::class.java)
            startActivity(intent)
        }
    }

    private fun dataRefreshManager() {
        handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                presenter?.getProject(accountID!!)
                handler.postDelayed(this, 2000)
            }
        })
    }


    override fun addProjectList(list: List<ProjectModel>) {
        (binding.rvProjectList.adapter as ProjectAdapter).addList(list)
    }

    override fun setError(error: String) {
        when (error){
            "Session Expired !" -> {
                sessionExpiredAlert()
                dialog.show()
            }

            "Project Not Found !" -> {
                binding.rvProjectList.visibility = View.GONE
                binding.notfound.visibility = View.VISIBLE
            }

            else -> {
                Toast.makeText(activity, error, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun hideProgressBar() {
        binding.loadingScreen.visibility = View.GONE
    }

    private fun sessionExpiredAlert() {
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

}