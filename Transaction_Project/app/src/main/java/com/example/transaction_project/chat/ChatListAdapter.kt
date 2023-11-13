package com.example.transaction_project.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.transaction_project.R

//목록창  adapter

class ChatListAdapter(private val chatRoom: List<ChatRoom>) : RecyclerView.Adapter<ChatListAdapter.ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_list, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chatRoomItem = chatRoom[position]
        holder.bind(chatRoomItem)
    }

    override fun getItemCount(): Int {
        return chatRoom.size
    }

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val name: TextView = itemView.findViewById(R.id.name)
        private val message: TextView = itemView.findViewById(R.id.message)
        private val productImage: ImageView = itemView.findViewById(R.id.productImage)
        private val checkRead: TextView = itemView.findViewById(R.id.checkRead)

        fun bind(chatRoomItem: ChatRoom) {
            name.text = chatRoomItem.sellerId
            message.text = chatRoomItem.message
            checkRead.text = chatRoomItem.checkRead
            checkRead.visibility = if (chatRoomItem.checkRead.isNotEmpty()) View.VISIBLE else View.INVISIBLE
        }
    }
}