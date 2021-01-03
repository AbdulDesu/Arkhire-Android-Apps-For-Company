package com.sizdev.arkhireforcompany.webviewer

interface WebListener {
    fun onPageStarted()
    fun onPageFinish()
    fun onShouldOverrideUrl(redirectUrl:String)
    fun onProgressChage(progress: Int)
    fun onReceivedError()
}