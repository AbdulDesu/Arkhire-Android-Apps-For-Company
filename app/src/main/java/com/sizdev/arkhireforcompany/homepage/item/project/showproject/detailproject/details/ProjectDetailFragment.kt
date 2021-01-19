package com.sizdev.arkhireforcompany.homepage.item.project.showproject.detailproject.details

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.administration.login.LoginActivity
import com.sizdev.arkhireforcompany.databinding.FragmentProjectDetailBinding
import com.sizdev.arkhireforcompany.homepage.item.project.showproject.ProjectResponse
import com.sizdev.arkhireforcompany.homepage.item.project.showproject.editproject.ProjectEditActivity
import com.sizdev.arkhireforcompany.networking.ArkhireApiClient
import com.sizdev.arkhireforcompany.networking.ArkhireApiService
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.alert_delete_project_confirmation.view.*
import kotlinx.android.synthetic.main.alert_session_expired.view.*
import kotlinx.coroutines.*
import java.text.NumberFormat
import java.util.*

class ProjectDetailFragment : Fragment(), ProjectDetailContract.View {

    private lateinit var binding: FragmentProjectDetailBinding
    private lateinit var dialog: AlertDialog
    private lateinit var handler: Handler
    private lateinit var coroutineScope: CoroutineScope

    private var presenter: ProjectDetailPresenter? = null
    private var projectID: String? =null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_project_detail, container, false)

        // Set Service
        setService()

        // Get Current Project Data
        getCurrentProjectData()

        // Show Progress Bar
        showProgressBar()

        // Data Refresh Management
        setDataRefreshManagement()

        // Button Manager
        binding.tvProjectDelete.setOnClickListener {
            projectDeleteConfirmation()
            dialog.show()
        }

        return  binding.root
    }


    override fun projectDeleteConfirmation() {
        val view: View = layoutInflater.inflate(R.layout.alert_delete_project_confirmation, null)

        dialog = this.let {
            activity?.let { it1 ->
                AlertDialog.Builder(it1)
                        .setView(view)
                        .setCancelable(false)
                        .create()
            }
        }!!

        view.bt_yesDelete.setOnClickListener {
            presenter?.deleteProject(projectID!!)
            dialog.dismiss()
            Toast.makeText(activity, "Project Deleted !", Toast.LENGTH_SHORT).show()
            activity?.finish()
        }

        view.bt_noDelete.setOnClickListener {
            dialog.cancel()
        }
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

    override fun setError(error: String) {
        when (error){
            "Session Expired !" -> {
                showSessionExpiredAlert()
                dialog.show()
            }

            "Project Clear !" -> {
                binding.tvProjectDelete.visibility = View.VISIBLE
            }

            else -> {
                Toast.makeText(activity, error, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun setService() {
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        val service = activity?.let { ArkhireApiClient.getApiClient(it)?.create(ArkhireApiService::class.java) }
        presenter = ProjectDetailPresenter(coroutineScope, service)
    }

    override fun getCurrentProjectData() {
        projectID = activity?.intent?.getStringExtra("projectID")
    }


    @SuppressLint("SetTextI18n")
    override fun setProjectData(projectID: String, projectTitle: String, projectDuration: String, projectDesc: String, projectSalary: String, projectImage: String, postedAt: String) {

        // Currency Formatter
        val format = NumberFormat.getCurrencyInstance()
        format.maximumFractionDigits = 0
        format.currency = Currency.getInstance("IDR")

        // Timestamp Convert
        val dateSplitter = postedAt.split("T")
        val timeSplitter = dateSplitter[1].split(".")

        binding.tvProjectTitle.text = projectTitle
        binding.tvProjectDuration.text = projectDuration

        if(projectSalary == "" || projectSalary.isEmpty()){
           binding.tvProjectSalary.text = "Data Malformed"
        }
        else{
            binding.tvProjectSalary.text = format.format(projectSalary.toDouble())
        }

        binding.tvProjectDesc.text = projectDesc
        binding.tvProjectCreated.text = "${dateSplitter[0]} - ${timeSplitter[0]}"

        Picasso.get()
                .load("http://54.82.81.23:911/image/${projectImage}")
                .resize(1280, 560)
                .centerCrop()
                .into(binding.ivProjectImage)

        // Enable Edit Button
        binding.tvProjectEdit.setOnClickListener {
            val intent = Intent(activity, ProjectEditActivity::class.java)
            intent.putExtra("projectID", projectID)
            intent.putExtra("projectTitle", projectTitle)
            intent.putExtra("projectDuration", projectDuration)
            intent.putExtra("projectSalary", projectSalary)
            intent.putExtra("projectDesc", projectDesc)
            startActivity(intent)
        }

        binding.helpProjectSalary.setOnClickListener {
            Toast.makeText(activity, "Expected Salary Of current project: $projectTitle", Toast.LENGTH_LONG).show()
        }

        binding.helpProjectDeadline.setOnClickListener {
            Toast.makeText(activity, "Deadline Of current project: $projectDuration", Toast.LENGTH_LONG).show()
        }

        binding.helpProjectCreated.setOnClickListener {
            Toast.makeText(activity, "Date Of created this project: $postedAt", Toast.LENGTH_LONG).show()
        }
    }

    override fun setDataRefreshManagement() {
        handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                presenter?.getProject(projectID!!)
                presenter?.verifyProject(projectID!!)
                handler.postDelayed(this, 5000)
            }
        })
    }

    override fun showProgressBar() {
        binding.loadingScreen.visibility = View.VISIBLE
        binding.progressBar.max = 100
    }

    override fun hideProgressBar() {
        binding.loadingScreen.visibility = View.GONE
    }
}