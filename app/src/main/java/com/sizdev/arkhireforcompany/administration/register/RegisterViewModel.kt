package com.sizdev.arkhireforcompany.administration.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sizdev.arkhireforcompany.networking.ArkhireApiService
import kotlinx.coroutines.*
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext

class RegisterViewModel: ViewModel(), CoroutineScope {

    val onSuccess = MutableLiveData<Boolean>()
    val onFail = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()

    private lateinit var service: ArkhireApiService

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setService(service: ArkhireApiService){
        this.service = service
    }

    fun startRegister(acName:String, acEmail:String, acPhone:String, password:String, privilege:Int, companyName: String, companyPosition: String) {
        launch {
            isLoading.value = true

            val result = withContext(Dispatchers.IO) {
                try {
                    service?.registerRequest(acName, acEmail, acPhone, password, privilege, companyName, companyPosition)
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main){
                        onSuccess.value = false
                        when{
                            e.code() == 400 -> {
                                onFail.value = "Malformed Data !"
                            }
                            e.code() == 409 -> {
                                onFail.value = "Account Has Been Registered !"
                            }
                            else -> {
                                onFail.value = "Unknown Error, Please Try Again Later !"
                            }
                        }
                    }
                }
            }

            if (result is RegisterResponse) {
                isLoading.value = false

                if(result.success) {
                    onSuccess.value = true
                }

                else {
                    onFail.value = result.message
                }
            }
        }
    }
}