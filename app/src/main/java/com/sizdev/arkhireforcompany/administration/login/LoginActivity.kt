package com.sizdev.arkhireforcompany.administration.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.administration.register.RegisterActivity
import com.sizdev.arkhireforcompany.administration.password.ForgetPasswordActivity
import com.sizdev.arkhireforcompany.databinding.ActivityLoginBinding
import com.sizdev.arkhireforcompany.homepage.HomeActivity
import com.sizdev.arkhirefortalent.networking.ApiClient
import kotlinx.coroutines.*


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: LoginAuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(this)!!.create(LoginAuthService::class.java)


        binding.btLogin.setOnClickListener {
            val email = binding.etLoginEmail.text.toString().toLowerCase()
            val password = binding.etLoginPassword.text.toString()

            if (email.isEmpty() && password.isEmpty()){
                Toast.makeText(this, "Please Fill Email & Password", Toast.LENGTH_LONG).show()
            }

            else {
                startLogin(email, password)
            }

        }

        binding.tvRegisterScreen.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.tvForgetPassword.setOnClickListener {
            val intent = Intent(this, ForgetPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun startLogin(email: String, password: String) {
        coroutineScope.launch {

            val result = withContext(Dispatchers.IO) {
                try {
                    service.loginRequest(email, password)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is LoginResponse) {

                if (result.message == "Success Login!") {

                    if(result.data?.privilege == "0"){
                        Toast.makeText(this@LoginActivity, "Welcome Back !", Toast.LENGTH_SHORT).show()

                        // Save Token
                        val sharedPref = this@LoginActivity.getSharedPreferences("Token", Context.MODE_PRIVATE)
                        val editor = sharedPref.edit()
                        editor.putString("accToken", result.data.token)
                        editor.putString("accID", result.data.userId)
                        editor.putString("accName", result.data.userName)
                        editor.apply()

                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        startActivity(intent)
                    }

                    else {
                        Toast.makeText(this@LoginActivity, "Please use talent version of Arkhire !", Toast.LENGTH_LONG).show()
                    }
                }

            }

            else {
                Toast.makeText(this@LoginActivity, "Invalid Email/Password", Toast.LENGTH_SHORT).show()
            }

        }
    }

}