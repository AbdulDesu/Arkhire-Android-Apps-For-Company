package com.sizdev.arkhireforcompany.homepage.item.project.createproject

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sizdev.arkhireforcompany.networking.ArkhireApiService
import kotlinx.coroutines.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import kotlin.coroutines.CoroutineContext

class CreateProjectViewModel: ViewModel(), CoroutineScope {
    val isSuccess = MutableLiveData<String>()
    private lateinit var service: ArkhireApiService

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setService(service: ArkhireApiService){
        this.service = service
    }

    fun createProject(ProjectTitle: RequestBody, ProjectDuration: RequestBody, ProjectDesc: RequestBody, ProjectSalary: RequestBody, ProjectOwner: RequestBody, ProjectImage: MultipartBody.Part) {
        launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.createProjectRequest(ProjectTitle, ProjectDuration, ProjectDesc, ProjectSalary, ProjectOwner, ProjectImage)
                } catch (e: Throwable) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main){
                        isSuccess.value = "Failed"
                    }
                }
            }

            if (result is CreateProjectResponse) {
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