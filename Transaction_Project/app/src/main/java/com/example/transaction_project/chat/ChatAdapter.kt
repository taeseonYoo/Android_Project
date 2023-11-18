package com.example.transaction_project.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.transaction_project.R
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


//처음 채팅을 시작하는 경우에 대한 해결 필요
class ChatAdapter(val chatList : ArrayList<ChatListItem>):
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder<*>>() {
    companion object{
        private const val SEND_CHAT = 0
        private const val RECIEVE_CHAT = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder<*> {
        return when(viewType){
            SEND_CHAT->{ //보내는 메세지 일때는 R.layout.send_chat
                val view = LayoutInflater.from(parent.context).inflate(R.layout.send_chat,parent,false)
                SendViewHolder(view)
            }
            RECIEVE_CHAT->{ //받는 메세지 일때는 R.layout.receive_chat
                val view = LayoutInflater.from(parent.context).inflate(R.layout.receive_chat,parent,false)
                ReceiveViewHolder(view)
            }
            else-> throw IllegalArgumentException("failed")
        }
    }

    override fun onBindViewHolder(holder: ChatViewHolder<*>, position: Int) {
        val message = chatList[position]
        when(holder){
            is SendViewHolder -> holder.bind(message)
            is ReceiveViewHolder -> holder.bind(message)
        }
    }

    //메세지 수신, 발신을 확인함
    override fun getItemViewType(position: Int): Int {

        val check = chatList[position].userId

        if(check=="2"){ //userId가 2일때 보내는 챗
            return SEND_CHAT
        }
        return RECIEVE_CHAT
    }
    override fun getItemCount(): Int {
        return chatList.count()
    }

    //뷰홀더 추상 클래스
    abstract class ChatViewHolder<ChatListItem>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: ChatListItem)
    }

    //뷰홀더 추상 클래스 상속함 -> 받은 메세지 사용
    inner class SendViewHolder(itemView: View) : ChatViewHolder<ChatListItem>(itemView) {
        override fun bind(item: ChatListItem) {
            val message = itemView.findViewById<TextView>(R.id.textView)
            val time = itemView.findViewById<TextView>(R.id.sendTime)
            message.text = item.message
            time.text = formatTime(item.timeAt)
        }
    }
    //뷰홀더 추상 클래스 상속함 -> 보내는 메세지 사용
    inner class ReceiveViewHolder(itemView: View) : ChatViewHolder<ChatListItem>(itemView) {
        override fun bind(item: ChatListItem) {
            val message = itemView.findViewById<TextView>(R.id.textView)
            val time = itemView.findViewById<TextView>(R.id.receiveTime)
            message.text = item.message
            time.text = formatTime(item.timeAt)
        }

    }
    //TimeFormatting ... timestamp to string(like a hh:mm)
    fun formatTime(timestamp: Timestamp): String {
        val format = SimpleDateFormat("a hh:mm", Locale.getDefault())
        return format.format(timestamp.toDate())
    }


}