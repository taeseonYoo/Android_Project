package com.example.transaction_project.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.transaction_project.R
import com.example.transaction_project.chat.ChatListAdapter
import com.example.transaction_project.chat.ChatListItem
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ChatFragment :Fragment(R.layout.chat_fragment) {
    private lateinit var chatRecyclerView : RecyclerView

    private val db: FirebaseFirestore = Firebase.firestore
    val chatCollectionRef = db.collection("Chat")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        chatRecyclerView = view.findViewById<RecyclerView>(R.id.chat_recyclerView)
        val chatList = ArrayList<ChatListItem>()
        //임의로 값 넣어서 테스트함. db연결해서 해야된다
        //시간 넣을지?
        //눌렀을때 채팅 화면으로 이동 구현해야한다



        chatList.add(ChatListItem("kim","eun","안녕하세요","","1"))
        chatList.add(ChatListItem("eun","kyoung","반가워요","","1"))
        chatList.add(ChatListItem("kyoung","kim","구매가능한가요?","",""))

        chatList.add(ChatListItem("김","은","안녕하세요!!","","1"))
        chatList.add(ChatListItem("은","경","반가워요!!","",""))
        chatList.add(ChatListItem("경","김","구매가능한가요~~?","",""))

        chatList.add(ChatListItem("홍길동","길동","안녕하세요!!","",""))
        chatList.add(ChatListItem("길동","홍길동","반가워요!!","",""))
        chatList.add(ChatListItem("뭉","맹","구매가능한가요~~?","",""))


        val chatAdapter = ChatListAdapter(chatList)
        chatRecyclerView.adapter = chatAdapter
        chatRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)


    }

}
