package com.example.transaction_project.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.transaction_project.R
import java.lang.IllegalArgumentException


//메세지를 DB로부터 가져 올 어댑터가 추가로 필요
//현재는 ID로 식별하여 리싸이클러뷰에 표시되도록 하였음

class ChatAdapter(val chatList : ArrayList<ChatListItem>):
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    companion object{
        private const val SEND_CHAT = 0
        private const val RECIEVE_CHAT = 1
    }

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

        if(nick=="1"){
            return SEND_CHAT
        }
        return RECIEVE_CHAT

    }
    override fun getItemCount(): Int {
        return chatList.count()
    }

    //메세지 어댑터를 완성하면 보내는 것, 받는 것 따로 작성해야함
    //추상 클래스 사용하기로한다.
//    inner class SendViewHolder(chatView: View) : ChatViewHolder<>(chatView) {
//
//
//
//    }
//    inner class RecieveViewHolder(chatView: View) : ChatViewHolder<>(chatView) {
//
//
//    }
//    abstract class ChatViewHolder<T>(chatView: View): RecyclerView.ViewHolder(chatView){
//        abstract fun set(itemView : T)
//    }



}