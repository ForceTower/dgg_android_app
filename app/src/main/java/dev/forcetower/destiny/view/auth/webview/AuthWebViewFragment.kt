package dev.forcetower.destiny.view.auth.webview

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.destiny.MemoryCookieJar
import dev.forcetower.destiny.core.model.chat.ChatMessage
import dev.forcetower.destiny.core.model.chat.ChatterConnection
import dev.forcetower.destiny.databinding.FragmentAuthWebviewBinding
import dev.forcetower.destiny.view.auth.options.AuthOptionsFragmentDirections
import dev.forcetower.toolkit.components.BaseFragment
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import timber.log.Timber

@AndroidEntryPoint
class AuthWebViewFragment : BaseFragment() {
    private val args by navArgs<AuthWebViewFragmentArgs>()
    private lateinit var binding: FragmentAuthWebviewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentAuthWebviewBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.webView.apply {
            webViewClient = LoginAwareWebViewClient(this@AuthWebViewFragment)
            postUrl("https://www.destiny.gg/login?authProvider=${args.provider}&rememberme=on", byteArrayOf())
            settings.apply {
                javaScriptEnabled = true
            }
        }
    }


    private fun onConnected(url: String, cookies: String) {
        val directions = AuthWebViewFragmentDirections.actionAuthWebViewToChat(url, cookies)
        findNavController().navigate(directions)
    }

    private class LoginAwareWebViewClient(private val fragment: AuthWebViewFragment) : WebViewClient() {
        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            val cookies = CookieManager.getInstance().getCookie(url)
            Timber.d("The URL: $url")
            Timber.d("The cookies $cookies")

            if (url.contains("destiny.gg/profile")) {
                Timber.d("Reached point...")
                fragment.onConnected(url, cookies)
            }
        }
    }
}