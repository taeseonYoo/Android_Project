package com.example.transaction_project.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.transaction_project.R
import com.example.transaction_project.chat.ChatListAdapter
import com.example.transaction_project.chat.ChatListItem
import com.example.transaction_project.chat.ChatRoom
import com.example.transaction_project.chat.ChatTestActivity
import com.example.transaction_project.home.Product
import com.example.transaction_project.login.UserInfo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ChatFragment :Fragment(R.layout.chat_fragment), ChatListAdapter.OnChatItemClickListener  {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var chatListAdapter: ChatListAdapter
    private val chatRoom = arrayListOf<ChatRoom>()

    private val db: FirebaseFirestore = Firebase.firestore
    private val chatRoomCollectionRef = db.collection("/ChatList/sellerId/Chat") //수정하기


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        chatRecyclerView = view.findViewById<RecyclerView>(R.id.chat_recyclerView)
        //val chatRoom = ArrayList<ChatRoom>()
        //임의로 값 넣어서 테스트함. db연결해서 해야된다
        //시간 넣을지?
        //눌렀을때 채팅 화면으로 이동 구현해야한다

        chatRoom.add(ChatRoom("eun","반가워요","","1"))
        chatRoom.add(ChatRoom("kyoung","구매가능한가요? 저거 꼭 사고싶은데요 ","",""))

        chatRoom.add(ChatRoom("김","안녕하세요!!","","1"))
        chatRoom.add(ChatRoom("은","반가워요!!","",""))
        chatRoom.add(ChatRoom("경","구매가능한가요~~?","",""))

        chatRoom.add(ChatRoom("홍길동","안녕하세요!!","",""))
        chatRoom.add(ChatRoom("길동","반가워요!!","",""))
        chatRoom.add(ChatRoom("뭉","구매가능한가요~~?","",""))


        chatListAdapter = ChatListAdapter(chatRoom, this)
        chatRecyclerView.adapter = chatListAdapter
        chatRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)


        //DB->CHATLIST->USERID->CHAT에 있는 컬렉션을 가져온다.

        //getChatRoomList()

    }

    override fun onItemClick(chatRoomItem: ChatRoom) {
        val intent = Intent(requireContext(), ChatTestActivity::class.java)
        // Pass necessary data to ChatTestActivity using intent extras if needed
        intent.putExtra("chatRoomId", chatRoomItem.sellerId)
        startActivity(intent)
    }

    private fun getChatRoomList() {
        chatRoomCollectionRef
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val chatRoomList = mutableListOf<ChatRoom>()
                    for (doc in task.result!!) {
                        val chatRoom = doc.toObject(ChatRoom::class.java)
                        chatRoomList.add(chatRoom)
                    }
                    //updateChatRoomList(chatRoomList)
                } else {
                    // Handle errors
                }
            }

    }



    /*
    private fun updateChatRoomList(chatRoomList: List<ChatRoom>) {
        val chatAdapter = ChatListAdapter(chatRoomList,this)
        chatRecyclerView.adapter = chatAdapter
        chatAdapter.notifyDataSetChanged()
    }

     */
}
/*
 테스트코드
 */
/* 태선님 코드
package com.example.transaction_project.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.transaction_project.R
import com.example.transaction_project.chat.ChatTestActivity

class ChatFragment :Fragment(R.layout.chat_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.test).setOnClickListener {
            val intent = Intent(requireContext(), ChatTestActivity::class.java)
            startActivity(intent)
        }
    }
}
 */
