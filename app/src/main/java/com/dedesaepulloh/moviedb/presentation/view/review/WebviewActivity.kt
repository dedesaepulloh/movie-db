package com.dedesaepulloh.moviedb.presentation.view.review

import android.graphics.Bitmap
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.dedesaepulloh.moviedb.R
import com.dedesaepulloh.moviedb.databinding.ActivityWebviewBinding
import com.dedesaepulloh.moviedb.utils.Constants.URL_ID

class WebviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebviewBinding

    private var url = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initIntent()
        initUi()
    }

    private fun initIntent() {
        url = intent.getStringExtra(URL_ID) ?: ""
    }

    private fun initUi() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.detail_review)

        binding.apply {
            mainWebview.settings.apply{
                javaScriptEnabled = true
                loadWithOverviewMode = true
                useWideViewPort = true
                domStorageEnabled = true
                allowContentAccess = true
                allowFileAccess = true
            }
            mainWebview.loadUrl("$url")
            mainWebview.webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView, newProgress: Int) {
                    progressBar.visibility = View.VISIBLE
                    progressBar.progress = newProgress
                    if (newProgress == 100) {
                        progressBar.visibility = View.GONE
                    }
                    super.onProgressChanged(view, newProgress)
                }
            }
            mainWebview.webViewClient = MyWebViewClient()
        }

    }

    private class MyWebViewClient : WebViewClient() {
        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
        }

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?,
        ): Boolean {
            view?.loadUrl(request?.url.toString())
            return true
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        binding.apply {
            if (keyCode == KeyEvent.KEYCODE_BACK && mainWebview.canGoBack()) {
                mainWebview.goBack()
                return true
            }
            return super.onKeyDown(keyCode, event)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

}