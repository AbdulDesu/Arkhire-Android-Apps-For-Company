package com.sizdev.arkhireforcompany.administration

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.homepage.HomeActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val sharedPrefData = this.getSharedPreferences("fullData", Context.MODE_PRIVATE)
        val registeredEmail = sharedPrefData.getString("companyEmail", null)
        val registeredPassword = sharedPrefData.getString("companyPassword", null)


        bt_login.setOnClickListener {
            val email = et_loginEmail.text.toString().toLowerCase()
            val password = et_loginPassword.text.toString()

            if(email == registeredEmail && password == registeredPassword){
                Toast.makeText(this, "Welcome Back", Toast.LENGTH_LONG).show()
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                logedInSuccesfully()
                finish()
            }

            else if (email.isEmpty() && password.isEmpty()){
                Toast.makeText(this, "Email & Password is empty", Toast.LENGTH_LONG).show()
            }

            else {
                Toast.makeText(this, "Invalid Email / Password", Toast.LENGTH_LONG).show()
            }


        }

        tv_registerScreen.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        tv_forgetPassword.setOnClickListener {
            val intent = Intent(this, ForgetPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun logedInSuccesfully(){
        val sharedPref = this.getSharedPreferences("User", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("Login", true)
        editor.apply()
    }
}