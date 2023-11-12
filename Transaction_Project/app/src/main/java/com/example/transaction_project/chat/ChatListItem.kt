package com.example.transaction_project.chat

data class ChatListItem(
    val userId : String,
    val sellerId : String,
    val message : String,
    val imgUrl : String,
    val checkRead : String //채팅 읽었는지 안 읽었는지
    //val itemName : String
    //추가 할 사항??
){constructor():this("","","","", "")}