package com.example.transaction_project.fragment

import android.content.ContentValues.TAG
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
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Date

class ChatFragment :Fragment(R.layout.chat_fragment), ChatListAdapter.OnChatItemClickListener {

    private val db: FirebaseFirestore = Firebase.firestore
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var chatListAdapter: ChatListAdapter
    private val chatRoomList = arrayListOf<ChatRoom>()
    private val myUid = Firebase.auth.currentUser?.uid
    //private val roomsCollectionRef = db.collection("ChatRoom")

    //itemview클릭시 이동
    override fun onItemClick(chatRoomList: ChatRoom) {
        val intent = Intent(requireContext(), ChatTestActivity::class.java)
        intent.putExtra("chatRoomId", chatRoomList.chatRoomId)
        startActivity(intent)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        chatRecyclerView = view.findViewById<RecyclerView>(R.id.chat_recyclerView)


        chatListAdapter = ChatListAdapter(chatRoomList, this)
        chatRecyclerView.adapter = chatListAdapter
        chatRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        loadChatList()

    }

    //내가 포함된  채팅DB를 불러온다. addsnapshotListener로 할지 고민
    private fun loadChatList() {
        val uid = myUid ?: return
        db.collection("ChatRoom")
            .whereArrayContains("author", uid)
            .get()
            .addOnSuccessListener { snapshot ->
                chatRoomList.clear() // Clear existing data
                for (document in snapshot.documents) {
                    val chatRoomId = document.id
                    val authors = document["author"] as List<String>
                    val otherUserUid = authors.find { it != uid }
                    if (otherUserUid != null) {
                        loadOtherUserInfo(chatRoomId, otherUserUid)
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }

    //상대방 정보 구해오기
    private fun loadOtherUserInfo(chatRoomId: String, otherUserUid: String) {
        db.collection("UserInfo").document(otherUserUid)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val otherUserName = document["name"] as String
                    val chatItem = ChatRoom(chatRoomId, otherUserName, "",  Timestamp(Date()), "")
                    chatRoomList.add(chatItem)

                    // 마지막 메시지 가져오기
                    loadLastMessage(chatRoomId)
                }
            }
    }

    //마지막 메세지 구하기. 시간별로 정렬해서 마지막 1개만 불러오도록 한다.
    private fun loadLastMessage(chatRoomId: String) {
        db.collection("ChatRoom")
            .document(chatRoomId)
            .collection("Chat")
            .orderBy("currentDate", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnSuccessListener { messages ->
                if (!messages.isEmpty) {
                    val lastMessage = messages.documents[0]["msg"] as String
                    chatRoomList.firstOrNull { it.chatRoomId == chatRoomId }?.lastMessage = lastMessage
                    chatListAdapter.notifyDataSetChanged()
                }
            }

    }

    //상품(아이템)의 itemId를 ItemList에서 찾고 일치하는 상품의 DB를 화면에 보여준다.
    private fun getItemInfo(itemId : String ,lastMessage : String){
        db.collection("/Items")
            .whereEqualTo("product", itemId ) //상품의 itemId와 일치하는 도큐먼트를 찾는다.
            .get()
            .addOnSuccessListener {
                for(doc in it){
                    chatRoomList.add(ChatRoom(doc["chatRoomId"] as String, "" , "" , Timestamp(Date()), doc["imgUrl"] as String))
                }
                chatListAdapter.notifyDataSetChanged()

            }
    }


}

