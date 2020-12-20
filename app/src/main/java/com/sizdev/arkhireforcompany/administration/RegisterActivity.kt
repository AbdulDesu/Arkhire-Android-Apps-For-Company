package com.sizdev.arkhireforcompany.administration

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.databinding.ActivityRegisterBinding
import com.sizdev.arkhireforcompany.homepage.HomeActivity
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private  lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)

        binding.btRegister.setOnClickListener {
            val registerPassword = binding.etRegistPassword.text.toString()
            val confirmRegisterPassword = binding.etConfirmRegistPassword.text.toString()
            val fullName = binding.etFullName.text.toString()
            val registerEmail = binding.etRegistEmail.text.toString()
            val registerPhoneNumber = binding.etRegistPhone.text.toString()
            val registerCompanyName = binding.etRegistCompanyName.text.toString()
            val registerCompanyPosition = binding.etRegistCompanyPosition.text.toString()

            if (registerPassword != confirmRegisterPassword){
                Toast.makeText(this, "Password not match", Toast.LENGTH_LONG).show()
            }
            else if (registerPassword.isEmpty() || confirmRegisterPassword.isEmpty() || fullName.isEmpty() || registerEmail.isEmpty() || registerPhoneNumber.isEmpty() || registerCompanyName.isEmpty() || registerCompanyPosition.isEmpty()){
                Toast.makeText(this, "Please Fill All Field", Toast.LENGTH_LONG).show()
            }
            else {
                Toast.makeText(this, "Registered Succesfully", Toast.LENGTH_LONG).show()
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                logedInSuccesfully()
                saveData()
                finish()
            }
        }
    }

    private fun saveData() {
        val fullName = binding.etFullName.text.toString()
        val registerEmail = binding.etRegistEmail.text.toString().toLowerCase()
        val registerPassword = binding.etRegistPassword.text.toString()
        val registerPhoneNumber = binding.etRegistPhone.text.toString()
        val registerCompanyName = binding.etRegistCompanyName.text.toString()
        val registerCompanyPosition = binding.etRegistCompanyPosition.text.toString()
        val sharedPrefData = getSharedPreferences("fullData", Context.MODE_PRIVATE)
        val editor = sharedPrefData.edit()
        editor.apply {
            putString("fullName", fullName)
            putString("companyEmail", registerEmail)
            putString("companyPassword", registerPassword)
            putString("companyPhone", registerPhoneNumber)
            putString("companyName", registerCompanyName)
            putString("companyPosition", registerCompanyPosition)
        }.apply()
    }

    private fun logedInSuccesfully(){
        val sharedPref = getSharedPreferences("User", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("Login", true)
        editor.apply()
    }



}