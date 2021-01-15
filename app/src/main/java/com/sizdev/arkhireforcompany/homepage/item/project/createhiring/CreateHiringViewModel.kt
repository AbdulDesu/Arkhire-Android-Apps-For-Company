package com.sizdev.arkhireforcompany.homepage.item.project.createhiring

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sizdev.arkhireforcompany.homepage.item.account.profile.edit.CompanyEditProfileResponse
import com.sizdev.arkhireforcompany.networking.ArkhireApiService
import kotlinx.coroutines.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext

class CreateHiringViewModel : ViewModel(), CoroutineScope {
    val onSuccess = MutableLiveData<Boolean>()
    val onFail = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()

    private lateinit var service: ArkhireApiService

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setService(service: ArkhireApiService){
        this.service = service
    }

    fun sendHiring( projectID: String, projectTarget: String, offeredSalary: String) {
        launch {
            isLoading.value = true

            val result = withContext(Dispatchers.IO) {
                try {
                    service?.hireTalentResponse(projectID, projectTarget, offeredSalary)
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main){
                        onSuccess.value = false
                        when{
                            e.code() == 400 -> {
                                onFail.value = "Malformed Data !"
                            }
                            else -> {
                                onFail.value = "Unknown Error, Please Try Again Later !"
                            }
                        }
                    }
                }
            }

            if (result is CreateHiringResponses) {
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