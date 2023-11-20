package com.example.transaction_project.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.transaction_project.R
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Locale


//메세지를 DB로부터 가져 올 어댑터가 추가로 필요
//현재는 ID로 식별하여 리싸이클러뷰에 표시되도록 하였음

class ChatAdapter(val chatList : ArrayList<ChatListItem>):
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    companion object{
        private const val SEND_CHAT = 0
        private const val RECIEVE_CHAT = 1
    }
    val uid = Firebase.auth.currentUser?.uid

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return when(viewType){
            SEND_CHAT->{ //보내는 메세지 일때는 R.layout.send_chat
                val view = LayoutInflater.from(parent.context).inflate(R.layout.send_chat,parent,false)
                ChatViewHolder(view)
            }
            RECIEVE_CHAT->{ //받는 메세지 일때는 R.layout.receive_chat
                val view = LayoutInflater.from(parent.context).inflate(R.layout.receive_chat,parent,false)
                ChatViewHolder(view)
            }
            else-> throw IllegalArgumentException("faild")
        }
    }

    inner class ChatViewHolder(chatView : View):RecyclerView.ViewHolder(chatView){


    }
    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {

    }

    override fun getItemViewType(position: Int): Int {
        //ChatListItem이라는 data 클래스에 식별 할 만한 것이 userID밖에 없어서 테스트
        val nick = chatList[position].userId

        val check = chatList[position].uid

        if(check== uid ){ //내가 보낸 메세지
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