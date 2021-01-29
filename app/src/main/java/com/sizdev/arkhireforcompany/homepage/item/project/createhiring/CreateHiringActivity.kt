package com.sizdev.arkhireforcompany.homepage.item.project.createhiring

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.databinding.ActivityCreateHiringBinding
import com.sizdev.arkhireforcompany.homepage.HomeActivity
import com.sizdev.arkhireforcompany.homepage.item.project.createproject.CreateProjectActivity
import com.sizdev.arkhireforcompany.homepage.item.project.showproject.ProjectModel
import com.sizdev.arkhireforcompany.homepage.item.project.showproject.ProjectResponse
import com.sizdev.arkhireforcompany.networking.ArkhireApiClient
import com.sizdev.arkhireforcompany.networking.ArkhireApiService
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.lang.Runnable
import java.text.NumberFormat
import java.util.*

class CreateHiringActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateHiringBinding
    private lateinit var viewModel: CreateHiringViewModel
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: ArkhireApiService
    private lateinit var handler: Handler

    private var projectTag: String? = null
    private var projectTitle: String? = null
    private var salaryExpectation: String? = null
    private var accountID: String? = null
    private var talentID: String? = null
    private var talentName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_hiring)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        viewModel = ViewModelProvider(this).get(CreateHiringViewModel::class.java)
        service = ArkhireApiClient.getApiClient(this)!!.create(ArkhireApiService::class.java)

        // Set Service
        setService()

        // Set Progress Bar
        binding.tvSendingData.visibility = View.GONE
        binding.loadingScreen.visibility = View.VISIBLE

        // Observe Data
        subscribeLiveData()

        // Get Current Login Data
        getCurentLoginData()

        // Get & Set Saved Talent Data
        getSetTalentData()

        // Show Project To Spinner
        setDataRefreshManager()

        // Enable Spinner
        binding.srSelectProject.adapter = CreateHiringSpinnerAdapter(this)
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    private fun verifyHiring() {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.checkHiredResponse(projectTag!!, talentName!!)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is CreateHiringResponses) {
                binding.btSendOffering.visibility = View.GONE
                binding.btHired.visibility = View.VISIBLE
                binding.btHired.setOnClickListener {
                    Toast.makeText(this@CreateHiringActivity, "$talentName already hired in $projectTitle", Toast.LENGTH_LONG).show()
                }
            }

            else {
                binding.btSendOffering.visibility = View.VISIBLE
                binding.btHired.visibility = View.GONE
                binding.btSendOffering.setOnClickListener {
                    sendOffering()
                }
            }

            binding.loadingScreen.visibility = View.GONE
        }
    }

    private fun getSetTalentData() {
        talentID = intent.getStringExtra("talentID")
        talentName = intent.getStringExtra("talentName")
        val talentTitle = intent.getStringExtra("talentTitle")
        val talentImage = intent.getStringExtra("talentImage")

        binding.tvTalentName.text = talentName
        binding.tvTalentTitle.text = talentTitle
        Picasso.get()
                .load("http://54.82.81.23:911/image/$talentImage")
                .resize(512, 512)
                .centerCrop()
                .into(binding.ivTalentImage)
    }

    private fun getCurentLoginData() {
        val sharedPrefData = this.getSharedPreferences("Token", Context.MODE_PRIVATE)
        accountID = sharedPrefData.getString("accID", null)
    }

    private fun showProject(accountID: String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.getAllProjectResponse(accountID)
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main){
                        when{
                            e.code() == 404 -> {
                                binding.loadingScreen.visibility = View.GONE
                                binding.notFound.visibility = View.VISIBLE
                                binding.btCreateProject.setOnClickListener {
                                    startActivity(Intent(this@CreateHiringActivity, CreateProjectActivity::class.java))
                                }
                            }
                            else -> {
                                Toast.makeText(this@CreateHiringActivity, "Unknown Error!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }

            if (result is ProjectResponse) {
                val list = result.data?.map {
                    ProjectModel(
                        it.projectID,
                        it.projectTitle,
                        it.projectDuration,
                        it.projectDesc,
                        it.projectSalary,
                        it.projectImage,
                        it.postedAt
                    )
                }

                (binding.srSelectProject.adapter as CreateHiringSpinnerAdapter).addList(list)

                binding.notFound.visibility = View.GONE

                binding.srSelectProject.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                        val spinner = list[position]
                        projectTag = spinner.projectID
                        salaryExpectation = spinner.projectSalary
                        projectTitle = spinner.projectTitle
                        verifyHiring()

                        // Enable Send Offering Feature
                        binding.btSendOffering.setOnClickListener {
                            sendOffering()
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
            }
        }
    }

    private fun sendOffering() {
        val talentID = intent.getStringExtra("talentID")
        val offeredSalary = binding.etOfferingSalary.text.toString()
        val format = NumberFormat.getCurrencyInstance()
        format.maximumFractionDigits = 0
        format.currency = Currency.getInstance("IDR")

        if (offeredSalary >= salaryExpectation!!){
            viewModel?.sendHiring(projectTag!!, talentID!!, offeredSalary)
        }
        else {
            Toast.makeText(this@CreateHiringActivity, "Salary Is Too low from expectation : ${format.format(salaryExpectation!!.toDouble())}", Toast.LENGTH_LONG).show()
        }
    }

    private fun setService() {
        val service = ArkhireApiClient.getApiClient(this)?.create(ArkhireApiService::class.java)
        if (service != null) {
            viewModel.setService(service)
        }
    }

    private fun subscribeLiveData() {
        viewModel.isLoading.observe(this, {
            binding.loadingScreen.visibility = View.VISIBLE
        })

        viewModel.onSuccess.observe(this, {
            if (it) {
                Toast.makeText(this, "Project sended !", Toast.LENGTH_LONG).show()
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }

            else {
                // Stop Loading
                binding.loadingScreen.visibility = View.GONE
            }
        })

        viewModel.onFail.observe(this, {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun setDataRefreshManager() {
        handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                showProject(accountID!!)
                handler.postDelayed(this, 2000)
            }
        })
    }
}