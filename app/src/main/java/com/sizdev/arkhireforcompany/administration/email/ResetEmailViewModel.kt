package com.sizdev.arkhireforcompany.administration.email

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sizdev.arkhireforcompany.networking.ArkhireApiService
import com.sizdev.arkhirefortalent.administration.email.ResetEmailResponse
import kotlinx.coroutines.*
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext

class ResetEmailViewModel: ViewModel(), CoroutineScope {

    val onSuccess = MutableLiveData<Boolean>()
    val onFail = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()

    private lateinit var service: ArkhireApiService

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setService(service: ArkhireApiService){
        this.service = service
    }

    fun resetEmail(accountID: String, email: String) {
        launch {
            isLoading.value = true
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.updateEmailResponse(accountID, email)
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main){
                        onSuccess.value = false
                        when{
                            e.code() == 404 -> {
                                onFail.value = "Account is not registered !"
                            }
                            else -> {
                                onFail.value = "Internal Server Error, Please Try Again Later !"
                            }
                        }
                    }
                }
            }

            if (result is ResetEmailResponse) {
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