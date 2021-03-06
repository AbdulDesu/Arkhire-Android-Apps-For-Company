package com.sizdev.arkhireforcompany.homepage.item.account.profile.edit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sizdev.arkhireforcompany.networking.ArkhireApiService
import kotlinx.coroutines.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import kotlin.coroutines.CoroutineContext

class CompanyEditProfileViewModel: ViewModel(), CoroutineScope {
    val isSuccess = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()

    private lateinit var service: ArkhireApiService

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setService(service: ArkhireApiService){
        this.service = service
    }

    fun updateCompany(companyID : String, companyLocation:RequestBody, companyLatitude:RequestBody, companyLongitude:RequestBody, companyType:RequestBody, companyDesc: RequestBody, companyLinkedin: RequestBody, companyInstagram: RequestBody, companyFacebook: RequestBody, companyImage: MultipartBody.Part) {
        launch {
            isLoading.value = true

            val result = withContext(Dispatchers.IO) {
                try {
                    service?.updateCompany(companyID, companyLocation, companyLatitude, companyLongitude, companyType, companyDesc, companyLinkedin, companyInstagram, companyFacebook, companyImage)
                } catch (e: Throwable) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main){
                        isSuccess.value = "Failed"
                    }
                }
            }

            if (result is CompanyEditProfileResponse) {
                isLoading.value = false
                if (result.success) {
                    isSuccess.value = "Success"
                }
                else {
                    isSuccess.value = "Failed"
                }
            }
        }
    }

    fun updateCompanyWithoutImage(companyID : String, companyLocation:String, companyLatitude:String, companyLongitude:String, companyType:String, companyDesc: String, companyLinkedin: String, companyInstagram: String, companyFacebook: String, companyImage: String) {
        launch {
            isLoading.value = false
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.updateCompanyWithoutImage(companyID, companyLocation, companyLatitude, companyLongitude, companyType, companyDesc, companyLinkedin, companyInstagram, companyFacebook, companyImage)
                } catch (e: Throwable) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main){
                        isSuccess.value = "Failed"
                    }
                }
            }

            if (result is CompanyEditProfileResponse) {
                isLoading.value = false
                if (result.success) {
                    isSuccess.value = "Success"
                }
                else {
                    isSuccess.value = "Failed"
                }
            }
        }
    }
}