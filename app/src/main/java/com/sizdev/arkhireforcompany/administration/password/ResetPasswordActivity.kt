package com.sizdev.arkhireforcompany.administration.password

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
import com.sizdev.arkhireforcompany.databinding.ActivityResetPasswordBinding
import com.sizdev.arkhireforcompany.networking.ArkhireApiClient
import com.sizdev.arkhireforcompany.networking.ArkhireApiService

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResetPasswordBinding
    private lateinit var viewModel: ResetPasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reset_password)

        viewModel = ViewModelProvider(this).get(ResetPasswordViewModel::class.java)

        // Set Service
        setService()

        // Observe Live Data
        subscribeLiveData()

        // Enable Click Listener
        binding.btUpdate.setOnClickListener {
            val sharedPref = this.getSharedPreferences("Token", Context.MODE_PRIVATE)

            if(binding.etNewPassword.text.isNullOrEmpty() || binding.etConfirmNewPassowrd.text.isNullOrEmpty() || binding.etVerifyPassword.text.isNullOrEmpty()){
                Toast.makeText(this, "Please Input All Required Field", Toast.LENGTH_SHORT).show()
            }

            else {
                if (binding.etNewPassword.text.toString() == binding.etConfirmNewPassowrd.text.toString()){
                    if(binding.etVerifyPassword.text.toString() == sharedPref.getString("accPassword", null).toString()){

                        if(binding.etNewPassword.text.toString().length >= 8){
                            viewModel.resetPassword(sharedPref.getString("accID", null).toString(), binding.etNewPassword.text.toString())
                            val editor = sharedPref.edit()
                            editor.putString("accPassword", binding.etNewPassword.text.toString())
                            editor.apply()
                        }
                        else {
                            Toast.makeText(this, "New Password Must Be 8 Character or more !", Toast.LENGTH_SHORT).show()
                        }

                    }
                    else {
                        Toast.makeText(this, "Invalid Current Password !", Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    Toast.makeText(
                        this, "Password Not Match !", Toast.LENGTH_SHORT
                    ).show()
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
                val intent = Intent(this, LoginActivity::class.java)
                val sharedPref = this.getSharedPreferences("Token", Context.MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.putString("accPassword", binding.etNewPassword.text.toString())
                editor.putString("accID", null)
                editor.apply()

                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                Toast.makeText(this, "Password Updated, Please Login Again To Continue !", Toast.LENGTH_LONG).show()
                startActivity(intent)
                finish()
            }
        })

        viewModel.onFail.observe(this, {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
    }
}