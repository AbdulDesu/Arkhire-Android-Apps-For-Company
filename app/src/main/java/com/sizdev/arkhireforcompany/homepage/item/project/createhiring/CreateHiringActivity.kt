package com.sizdev.arkhireforcompany.homepage.item.project.createhiring

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.databinding.ActivityCreateHiringBinding
import com.sizdev.arkhireforcompany.homepage.HomeActivity
import com.sizdev.arkhireforcompany.homepage.item.project.showproject.ProjectModel
import com.sizdev.arkhireforcompany.homepage.item.project.showproject.ProjectResponse
import com.sizdev.arkhireforcompany.networking.ArkhireApiClient
import com.sizdev.arkhireforcompany.networking.ArkhireApiService
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*

class CreateHiringActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateHiringBinding
    private lateinit var viewModel: CreateHiringViewModel
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: ArkhireApiService
    private var projectTag: String? = null
    private var salaryExpectation: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_hiring)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        viewModel = ViewModelProvider(this).get(CreateHiringViewModel::class.java)
        service = ArkhireApiClient.getApiClient(this)!!.create(ArkhireApiService::class.java)

        // Set Service
        setService()
        subscribeLiveData()

        // Get Saved ID
        val sharedPrefData = this.getSharedPreferences("Token", Context.MODE_PRIVATE)
        val savedID = sharedPrefData.getString("accID", null)

        // Get Saved Data
        val talentName = intent.getStringExtra("talentName")
        val talentTitle = intent.getStringExtra("talentTitle")
        val talentImage = intent.getStringExtra("talentImage")

        // Set Saved Data
        binding.tvTalentName.text = talentName
        binding.tvTalentTitle.text = talentTitle
        Picasso.get()
            .load("http://54.82.81.23:911/image/$talentImage")
            .resize(512, 512)
            .centerCrop()
            .into(binding.ivTalentImage)

        // Show Project To Spinner
        showProject(savedID!!)

        // Enable Spinner
        binding.srSelectProject.adapter = CreateHiringSpinnerAdapter(this)


    }

    private fun showProject(accountID: String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getAllProjectResponse(accountID)
                } catch (e: Throwable) {
                    e.printStackTrace()
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

                binding.srSelectProject.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                        val spinner = list[position]
                        projectTag = spinner.projectID
                        salaryExpectation = spinner.projectSalary
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }

                // Enable Send Offering Feature
                binding.btSendOffering.setOnClickListener {

                    // Get Data Saved
                    val talentID = intent.getStringExtra("talentID")
                    val offeredSalary = binding.etOfferingSalary.text.toString()

                    if (offeredSalary >= salaryExpectation!!){
                        viewModel?.sendHiring(projectTag!!, talentID!!, offeredSalary)
                    }
                    else {
                        Toast.makeText(this@CreateHiringActivity, "Salary Is Too low from expecation : $salaryExpectation", Toast.LENGTH_LONG).show()
                    }
                }
                
            }
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

}