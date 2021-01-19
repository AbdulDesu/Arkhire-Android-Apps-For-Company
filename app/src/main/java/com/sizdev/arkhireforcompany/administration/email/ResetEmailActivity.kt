package com.sizdev.arkhireforcompany.administration.email

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.administration.login.LoginActivity
import com.sizdev.arkhireforcompany.databinding.ActivityResetEmailBinding
import com.sizdev.arkhireforcompany.networking.ArkhireApiClient
import com.sizdev.arkhireforcompany.networking.ArkhireApiService

class ResetEmailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResetEmailBinding
    private lateinit var viewModel: ResetEmailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reset_email)
        viewModel = ViewModelProvider(this).get(ResetEmailViewModel::class.java)

        // Set Service
        setService()

        // Observe Live Data
        subscribeLiveData()

        // Set Button
        binding.btUpdate.setOnClickListener {
            val sharedPrefData = this.getSharedPreferences("Token", Context.MODE_PRIVATE)

            if(binding.etNewEmail.text.isNullOrEmpty() || binding.etVerifyPassword.text.isNullOrEmpty()){
                Toast.makeText(this, "Please Fill All Required Field !", Toast.LENGTH_SHORT).show()
            }

            else {
                if(binding.etVerifyPassword.text.toString() == sharedPrefData.getString("accPassword", null)){
                    viewModel.resetEmail(sharedPrefData.getString("accID", null).toString(), binding.etNewEmail.text.toString())
                }
                else{
                    Toast.makeText(this, "Wrong Password !", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun subscribeLiveData() {
        viewModel.isLoading.observe(this, {
            binding.loadingScreen.visibility = View.VISIBLE
        })

        viewModel.onSuccess.observe(this, {
            if (it) {
                val intent = Intent(this, LoginActivity::class.java)
                val sharedPref = this.getSharedPreferences("Token", Context.MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.putString("accEmail", binding.etNewEmail.text.toString())
                editor.putString("accID", null)
                editor.apply()

                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                Toast.makeText(this, "Email Updated, Please Login Again To Continue !", Toast.LENGTH_LONG).show()
                startActivity(intent)
                finish()
            }
        })

        viewModel.onFail.observe(this, {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun setService() {
        val service = ArkhireApiClient.getApiClient(this)?.create(ArkhireApiService::class.java)
        if (service != null) {
            viewModel.setService(service)
        }

        val sharedPref = this.getSharedPreferences("Token", Context.MODE_PRIVATE)
        binding.tvRegisteredEmail.text = sharedPref.getString("accEmail", null)
    }
}