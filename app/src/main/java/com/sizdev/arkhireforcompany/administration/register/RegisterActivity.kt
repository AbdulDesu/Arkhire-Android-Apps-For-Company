package com.sizdev.arkhireforcompany.administration.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.administration.login.LoginActivity
import com.sizdev.arkhireforcompany.databinding.ActivityRegisterBinding
import com.sizdev.arkhireforcompany.networking.ArkhireApiClient
import com.sizdev.arkhireforcompany.networking.ArkhireApiService
import kotlinx.coroutines.*


class RegisterActivity : AppCompatActivity() {

    private  lateinit var binding: ActivityRegisterBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: ArkhireApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ArkhireApiClient.getApiClient(this)!!.create(ArkhireApiService::class.java)

        binding.btRegister.setOnClickListener {
            val registerPassword = binding.etRegistPassword.text.toString()
            val confirmRegisterPassword = binding.etConfirmRegistPassword.text.toString()
            val fullName = binding.etFullName.text.toString()
            val registerEmail = binding.etRegistEmail.text.toString().toLowerCase()
            val registerPhoneNumber = binding.etRegistPhone.text.toString()
            val registerCompanyName = binding.etRegistCompanyName.text.toString()
            val registerCompanyPosition = binding.etRegistCompanyPosition.text.toString()

           if (registerPassword.isEmpty() || confirmRegisterPassword.isEmpty() || fullName.isEmpty() || registerEmail.isEmpty() || registerPhoneNumber.isEmpty() || registerCompanyName.isEmpty() || registerCompanyPosition.isEmpty()){
                Toast.makeText(this, "Please Fill All Field", Toast.LENGTH_LONG).show()
            }
            else {
               if (registerPassword != confirmRegisterPassword){
                   Toast.makeText(this, "Password not match", Toast.LENGTH_LONG).show()
               }
               else {
                   startRegister(fullName,registerEmail,registerPhoneNumber, registerPassword, 1, registerCompanyName, registerCompanyPosition)
               }
            }
        }

        binding.tvBackLogin.setOnClickListener {
            finish()
        }

    }

    private fun startRegister(acName:String, acEmail:String, acPhone:String, password:String, privilege:Int, companyName: String, companyPosition: String) {
        coroutineScope.launch {

            val result = withContext(Dispatchers.IO) {
                try {
                    service.registerRequest(acName,acEmail,acPhone,password,privilege,companyName,companyPosition)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (result is RegisterResponse) {
                Toast.makeText(this@RegisterActivity, "Registered Succesfully, Please Login To Continue", Toast.LENGTH_LONG).show()
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

}