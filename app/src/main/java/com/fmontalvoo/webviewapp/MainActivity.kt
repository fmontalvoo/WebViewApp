package com.fmontalvoo.webviewapp

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.*
import android.widget.SearchView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val BASE_URL = "https://www.google.com.ec"
    private val SEARCH_PATH = "search?q"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//      WebView
        webView.webChromeClient = object : WebChromeClient() {

        }

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                searchView.setQuery(url, false)
                swipeRefresh.isRefreshing = true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                swipeRefresh.isRefreshing = false
            }
        }

        val settings: WebSettings = webView.settings;
        settings.javaScriptEnabled = true

        webView.loadUrl(BASE_URL)

//      Search
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (URLUtil.isValidUrl(it))
                        webView.loadUrl(it)
                    else
                        webView.loadUrl("$BASE_URL/$SEARCH_PATH=$it")
                }
                return false
            }
        })

//      Refresh
        swipeRefresh.setOnRefreshListener {
            webView.reload()
        }
    }
    
    override fun onBackPressed() {
        if (webView.canGoBack())
            webView.goBack()
        else
            super.onBackPressed()
    }
}