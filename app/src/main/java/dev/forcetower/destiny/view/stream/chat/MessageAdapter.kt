package dev.forcetower.destiny.view.stream.chat

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.forcetower.destiny.R
import dev.forcetower.destiny.core.model.chat.ChatMessage
import dev.forcetower.destiny.databinding.ItemChatMessageBinding
import dev.forcetower.toolkit.extensions.inflate

class MessageAdapter : ListAdapter<ChatMessage, MessageAdapter.ChatHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageAdapter.ChatHolder {
        return ChatHolder(parent.inflate(R.layout.item_chat_message))
    }

    override fun onBindViewHolder(holder: MessageAdapter.ChatHolder, position: Int) {
        holder.binding.message = getItem(position)
    }

    inner class ChatHolder(val binding: ItemChatMessageBinding) : RecyclerView.ViewHolder(binding.root)

    private object DiffCallback : DiffUtil.ItemCallback<ChatMessage>() {
        override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
            return oldItem.data == newItem.data && oldItem.nick == newItem.nick
        }

        override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
            return oldItem == newItem
        }
    }
}