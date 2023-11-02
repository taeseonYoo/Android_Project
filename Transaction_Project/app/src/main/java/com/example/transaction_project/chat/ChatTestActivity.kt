package com.example.transaction_project.chat


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.transaction_project.R
import com.example.transaction_project.home.ItemAdapter
import com.example.transaction_project.home.Product


class ChatTestActivity :AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_test)


        val recyclerView = findViewById<RecyclerView>(R.id.message)

        //전반적인 이미지를 확인하기 위하여 직접 메세지 넣음, 차후에 DB를 이용
        val chatList = ArrayList<ChatListItem>()
        chatList.add(ChatListItem("2","1"))
        chatList.add(ChatListItem("1","2"))


        val chatAdapter = ChatAdapter(chatList)
        chatAdapter.notifyDataSetChanged()

        recyclerView.adapter = chatAdapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

    }
}