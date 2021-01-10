package dev.forcetower.destiny.view.stream.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.postDelayed
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.destiny.MemoryCookieJar
import dev.forcetower.destiny.core.model.chat.ChatMessage
import dev.forcetower.destiny.core.model.chat.ChatterConnection
import dev.forcetower.destiny.databinding.FragmentChatBinding
import dev.forcetower.toolkit.components.BaseFragment
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import timber.log.Timber

@AndroidEntryPoint
class ChatFragment : BaseFragment() {
    private val args by navArgs<ChatFragmentArgs>()
    private lateinit var binding: FragmentChatBinding
    private lateinit var adapter: MessageAdapter

    private val gson = Gson()

    private val messages = mutableListOf<ChatMessage>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = FragmentChatBinding.inflate(inflater, container, false).also {
            binding = it
        }.root

        adapter = MessageAdapter()
        binding.messagesRecycler.apply {
            adapter = this@ChatFragment.adapter
            itemAnimator?.apply {
                changeDuration = 0
                removeDuration = 0
                moveDuration = 0
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        connectToChat()
    }

    private fun connectToChat() {
        val url = args.url
        val cookies = args.cookies

        val http = url.toHttpUrl()

        val headers = Headers.Builder()
            .add("Set-Cookie", cookies)
            .build()

        val values = Cookie.parseAll(http, headers)

        Timber.d("Element $values")

        val cookieJar = MemoryCookieJar().apply {
            saveFromResponse(http, values)
        }
        val client = OkHttpClient.Builder()
            .cookieJar(cookieJar)
            .build()

        val request = Request.Builder()
            .url("wss://chat.destiny.gg/ws")
            .build()

        val regex = Regex("([a-zA-Z0-9]*) (\\{.*\\})")

        client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                Timber.d("Opened!")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                try {
                    val result = regex.find(text)
                    if (result != null && result.groupValues.size == 3) {
                        val command = result.groupValues[1]
                        val json = result.groupValues[2]
                        when (command) {
                            "JOIN" -> onChatterConnection(json, true)
                            "QUIT" -> onChatterConnection(json, false)
                            "MSG" -> onMessageReceived(json)
                            "NAMES" -> onChattersUpdate(json)
                        }
                    } else {
                        Timber.e("Invalid message: $text")
                    }
                } catch (error: Throwable) {
                    Timber.e(error, "Error")
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                Timber.d(t, "Error!")
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                Timber.d("Closed $code $reason")
            }
        })
    }

    private fun onChattersUpdate(json: String) {
        Timber.d("Chatters connected $json")
    }

    private fun onMessageReceived(json: String) {
        val message = gson.fromJson(json, ChatMessage::class.java)
        Timber.d("Message received: $message")
        val messagesNew = messages + message
        messages.add(message)
        binding.root.post {
            adapter.submitList(messagesNew)
            binding.root.postDelayed(100) {
                binding.messagesRecycler.scrollToPosition(messagesNew.size - 1)
            }
        }
    }

    private fun onChatterConnection(json: String, join: Boolean) {
        val connection = gson.fromJson(json, ChatterConnection::class.java)
        Timber.d("Connection: $join --> $connection")
    }
}