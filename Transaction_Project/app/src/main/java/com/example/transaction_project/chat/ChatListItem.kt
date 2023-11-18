package com.example.transaction_project.chat

import com.google.firebase.Timestamp

data class ChatListItem(
    val userId : String,
    val sellerId : String,
    val message : String,
    val timeAt : Timestamp,
    var itemImgUrl: String = "",
    var itemPrice: String = "",
    var itemTitle: String = ""
    //추가 할 사항??
){constructor():this("","","",Timestamp.now())}