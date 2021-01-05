package com.sizdev.arkhireforcompany.webviewer

interface WebListener {
    fun onPageStarted()
    fun onPageFinish()
    fun onShouldOverrideUrl(redirectUrl:String)
    fun onProgressChange(progress: Int)
    fun onReceivedError()
}