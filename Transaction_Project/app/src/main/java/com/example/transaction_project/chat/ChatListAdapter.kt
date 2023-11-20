package com.example.transaction_project.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.transaction_project.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Locale

//목록창  adapter

class ChatListAdapter(private val chatRoomList: List<ChatRoom>,  private val onItemClickListener: OnChatItemClickListener)
    : RecyclerView.Adapter<ChatListAdapter.ChatViewHolder>() {


    interface OnChatItemClickListener {
        fun onItemClick(chatRoomItem: ChatRoom)
    }


    fun submitList(newList: List<ChatRoom>) {
        submitList(ArrayList(newList))
    }


    //레이아웃 연결
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_list, parent, false)
        return ChatViewHolder(view)
    }

    //데이터 연결
    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chatRoomItem = chatRoomList[position]
        holder.bind(chatRoomItem)
        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(chatRoomItem)
        }

    }

    override fun getItemCount(): Int {
        return chatRoomList.size
    }

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val otherName: TextView = itemView.findViewById(R.id.name)
        private val lastMessage: TextView = itemView.findViewById(R.id.message)
        private val productImage: ImageView = itemView.findViewById(R.id.productImage)
        //private val checkRead: TextView = itemView.findViewById(R.id.checkRead)


        fun bind(chatRoomItem: ChatRoom) {
            otherName.text = chatRoomItem.otherUserName
            lastMessage.text = chatRoomItem.lastMessage
            if(chatRoomItem.imgUrl!==""){
                Glide.with(productImage)
                    .load(chatRoomItem.imgUrl)
                    .error(R.drawable.user)
                    .into(productImage)

            }

            //val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())
            //val date = chatRoomItem.currentDate.toDate()
            //val formattedDate = dateFormat.format(date)
            //lastMessageTime.text = formattedDate

            // checkRead.text = chatRoomItem.checkRead
            //checkRead.visibility = if (chatRoomItem.checkRead.isNotEmpty()) View.VISIBLE else View.INVISIBLE
        }

    }
}