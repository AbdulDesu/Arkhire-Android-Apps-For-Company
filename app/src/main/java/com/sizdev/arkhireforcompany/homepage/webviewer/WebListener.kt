package com.sizdev.arkhireforcompany.homepage.webviewer

interface WebListener {
    fun onPageStarted()
    fun onPageFinish()
    fun onShouldOverrideUrl(redirectUrl:String)
    fun onProgressChage(progress: Int)
}