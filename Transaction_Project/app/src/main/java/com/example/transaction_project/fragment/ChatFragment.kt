package com.example.transaction_project.fragment

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.transaction_project.FirestoreInstance
import com.example.transaction_project.R
import com.example.transaction_project.chat.ChatActivity
import com.example.transaction_project.chat.ChatListAdapter
import com.example.transaction_project.chat.ChatRoom
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import java.util.Date

class ChatFragment :Fragment(R.layout.chat_fragment), ChatListAdapter.OnChatItemClickListener  {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var chatListAdapter: ChatListAdapter
    private val chatRoom = arrayListOf<ChatRoom>()
    private val uid = FirestoreInstance.auth.currentUser?.uid

    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        initChatListAdapter(requireView())
        loadChatList()
    }

    private fun initChatListAdapter(view: View){
        chatRecyclerView = view.findViewById<RecyclerView>(R.id.chat_recyclerView)
        chatListAdapter = ChatListAdapter(chatRoom, this)
        chatRecyclerView.adapter = chatListAdapter
        chatRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }


    //상품 목록이 클릭 되면 , 도큐먼트id와 itemid를 담아서 채팅액티비티 시작
    override fun onItemClick(chatRoomItem: ChatRoom) {
        val intent = Intent(requireContext(), ChatActivity::class.java)

        intent.putExtra("chatRoomId",chatRoomItem.chatRoomId)
        intent.putExtra("productId",chatRoomItem.productId)
        startActivity(intent)
    }

    //내가 포함된  채팅DB를 불러온다. addsnapshotListener로 할지 고민
    private fun loadChatList() {

        val uid = uid ?: return
        FirestoreInstance.chatRoomListRef
            .whereArrayContains("authors", uid)
            .get()
            .addOnSuccessListener { snapshot ->
                chatRoom.clear() // Clear existing data
                for (document in snapshot.documents) {
                    val chatRoomId = document.id
                    val productId = document["productId"] as String
                    val authors = document["authors"] as List<String>
                    val otherUserUid = authors.find { it != uid }
                    if (otherUserUid != null) {
                        loadOtherUserInfo(chatRoomId, otherUserUid, productId)
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }


    //상대방 정보 구해오기
    private fun loadOtherUserInfo(chatRoomId: String, otherUserUid: String, productId : String) {
        FirestoreInstance.userInfoRef
            .document(otherUserUid)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val otherUserName = document["name"] as String
                    val chatItem = ChatRoom(chatRoomId, otherUserName, "",  Timestamp(Date()),"" , productId)
                    chatRoom.add(chatItem)

                    // 마지막 메시지 가져오기
                    getItemInfo(productId)
                    loadLastMessage(chatRoomId)
                }
            }
    }
    //마지막 메세지 구하기. 시간별로 정렬해서 마지막 1개만 불러오도록 한다.
    private fun loadLastMessage(chatRoomId: String) {
        FirestoreInstance.chatRoomListRef
            .document(chatRoomId)
            .collection("/Chat")
            .orderBy("timeAt", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnSuccessListener { messages ->
                if (!messages.isEmpty) {
                    val lastMessage = messages.documents[0]["message"] as String
                    chatRoom.firstOrNull { it.chatRoomId == chatRoomId }?.lastMessage = lastMessage
                    chatListAdapter.notifyDataSetChanged()
                }
            }

    }
    private fun getItemInfo(productId : String ) {
        FirestoreInstance.itemListRef
            .whereEqualTo("productId", productId) //상품의 productId와 일치하는 도큐먼트를 찾는다.
            .get()
            .addOnSuccessListener {documents->
                if (!documents.isEmpty) {
                    val document = documents.documents[0]
                    val imgUrl = document["imgUrl"] as String
                    chatRoom.firstOrNull { it.productId == productId }?.imgUrl = imgUrl
                    chatListAdapter.notifyDataSetChanged()
                }
            }

    }



}
