package com.sizdev.arkhireforcompany.homepage.item.home.detailedtalent

import com.sizdev.arkhireforcompany.networking.ArkhireApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class TalentProfilePresenter(private val coroutineScope: CoroutineScope,
                             private val service: ArkhireApiService?) : TalentProfileContract.Presenter {

    private var view: TalentProfileContract.View? = null

    override fun bindToView(view: TalentProfileContract.View) {
        this.view = view
    }

    override fun unbind() {
        this.view = null
    }

    override fun getTalentByID(talentID: String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getTalentByIDResponse(talentID)
                } catch (e: HttpException) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main){
                        when{
                            e.code() == 403 -> {
                                view?.setError("Session Expired !")
                            }

                            else -> {
                                view?.setError("Unknown Error, Please Try Again Later !")
                            }
                        }
                    }
                }
            }

            if (result is TalentProfileResponse) {
                if (result.success){
                    view?.hideProgressBar()
                    view?.setTalentData(result.data[0].talentID, result.data[0].accountName, result.data[0].accountEmail, result.data[0].accountPhone, result.data[0].talentTitle, result.data[0].talentTime, result.data[0].talentCity, result.data[0].talentDesc, result.data[0].talentImage, result.data[0].talentGithub, result.data[0].talentCv, result.data[0].talentSkill1, result.data[0].talentSkill2, result.data[0].talentSkill3, result.data[0].talentSkill4, result.data[0].talentSkill5)
                }
                else {
                    view?.hideProgressBar()
                    view?.setError(result.message)
                }
            }
        }
    }
}