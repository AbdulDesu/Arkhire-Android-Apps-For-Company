package com.sizdev.arkhireforcompany.administration.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sizdev.arkhireforcompany.networking.ArkhireApiService
import kotlinx.coroutines.*
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext

class LoginViewModel: ViewModel(), CoroutineScope {

    val onSuccess = MutableLiveData<Boolean>()
    val onFail = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()
    val loginData = MutableLiveData<LoginResponse.Data>()

    private lateinit var service: ArkhireApiService

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setService(service: ArkhireApiService){
        this.service = service
    }

    fun startLogin(email: String, password: String) {
        launch {
            isLoading.value = true

            val result = withContext(Dispatchers.IO) {
                try {
                    service?.loginRequest(email, password)
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main){
                        onSuccess.value = false
                        when{
                            e.code() == 404 -> {
                                onFail.value = "Account is not registered !"
                            }
                            e.code() == 405 -> {
                                onFail.value = "Invalid Password !"
                            }
                            else -> {
                                onFail.value = "Internal Server Error, Please Try Again Later !"
                            }
                        }
                    }
                }
            }

            if (result is LoginResponse) {
                isLoading.value = false

                if(result.success) {
                    loginData.value = result.data

                    onSuccess.value = true
                }

                else {
                    onFail.value = result.message
                }
            }
        }
    }

}