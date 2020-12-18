package com.sizdev.arkhireforcompany.administration

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.homepage.HomeActivity
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        bt_register.setOnClickListener {
            val registerPassword = et_registPassword.text.toString()
            val confirmRegisterPassword = et_confirmRegistPassword.text.toString()
            val fullName = et_fullName.text.toString()
            val registerEmail = et_registEmail.text.toString().toLowerCase()
            val registerPhoneNumber = et_registPhone.text.toString()
            val registerCompanyName = et_registCompany.text.toString()
            val registerCompanyPosition = et_registCompanyPosition.text.toString()

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
        val fullName = et_fullName.text.toString()
        val registerEmail = et_registEmail.text.toString().toLowerCase()
        val registerPassword = et_registPassword.text.toString()
        val registerPhoneNumber = et_registPhone.text.toString()
        val registerCompanyName = et_registCompany.text.toString()
        val registerCompanyPosition = et_registCompanyPosition.text.toString()
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