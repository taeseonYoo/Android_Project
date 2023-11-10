package com.example.transaction_project.chat

data class ChatListItem(
    val userId : String,
    val sellerId : String,
    val message : String
    //추가 할 사항??
){constructor():this("","","")}
