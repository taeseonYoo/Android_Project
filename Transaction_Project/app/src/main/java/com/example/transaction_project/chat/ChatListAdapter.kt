package com.example.transaction_project.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.transaction_project.R

class ChatListAdapter(private val chatList: List<ChatListItem>) : RecyclerView.Adapter<ChatListAdapter.ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_list, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chatItem = chatList[position]
        holder.bind(chatItem)
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userImage: ImageView = itemView.findViewById(R.id.userImage)
        private val name: TextView = itemView.findViewById(R.id.name)
        private val message: TextView = itemView.findViewById(R.id.message)
        private val productImage: ImageView = itemView.findViewById(R.id.productImage)
        private val checkRead: TextView = itemView.findViewById(R.id.checkRead)

        fun bind(chatItem: ChatListItem) {
            name.text = chatItem.userId
            message.text = chatItem.message
            checkRead.text = chatItem.checkRead
            checkRead.visibility = if (chatItem.checkRead.isNotEmpty()) View.VISIBLE else View.INVISIBLE
        }
    }
}