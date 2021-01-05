package com.sizdev.arkhireforcompany.webviewer

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.webkit.*
import androidx.databinding.DataBindingUtil
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.databinding.ActivityArkhireWebViewerBinding

class ArkhireWebViewerActivity : AppCompatActivity(), WebListener {

    private lateinit var binding: ActivityArkhireWebViewerBinding
    private var doubleBackToExitPressedOnce = false

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_arkhire_web_viewer)

        val url = intent.getStringExtra("url")
        val scale = intent.getStringExtra("webScale")

        setSupportActionBar(binding.tbArkhireBrowser)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.tbArkhireBrowser.setNavigationOnClickListener {
            finish()
        }
        if ( scale!= null ){
            binding.wvArkhireWebViewer.setInitialScale(scale.toInt())
        }
        binding.wvArkhireWebViewer.loadUrl("$url")
        binding.wvArkhireWebViewer.webChromeClient = ArkhireChromeClient(this)
        binding.wvArkhireWebViewer.webViewClient = ArkhireWebClient(this)
        binding.wvArkhireWebViewer.settings.javaScriptEnabled = true
    }

    class ArkhireChromeClient(private var listener:WebListener) : WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            listener.onProgressChange(newProgress)
        }
    }

    class ArkhireWebClient(private var listener: WebListener):WebViewClient(){
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            listener.onPageStarted()
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            listener.onPageFinish()
        }

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            listener.onShouldOverrideUrl(request?.url?.toString().orEmpty())
            return super.shouldOverrideUrlLoading(view, request)
        }

        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            super.onReceivedError(view, request, error)
            listener.onReceivedError()
        }

    }

    override fun onPageStarted() {
        binding.arkhireWebProgressBar.visibility = View.VISIBLE
    }

    override fun onPageFinish() {
        binding.arkhireWebProgressBar.visibility = View.GONE
    }

    override fun onShouldOverrideUrl(redirectUrl: String) {

    }

    override fun onProgressChange(progress: Int) {
        binding.arkhireWebProgressBar.progress = progress
    }

    override fun onReceivedError() {
        binding.notFound.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        if (binding.wvArkhireWebViewer.canGoBack()) {
            binding.wvArkhireWebViewer.goBack();
        }

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true

        Handler().postDelayed(Runnable {
            doubleBackToExitPressedOnce = false
        }, 2000)
    }
}