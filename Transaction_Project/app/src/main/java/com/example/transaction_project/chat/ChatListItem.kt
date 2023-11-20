package com.example.transaction_project.chat

import com.google.firebase.Timestamp

data class ChatListItem(
    val uid : String,
    val message : String,
    val timeAt : Timestamp
    //추가 할 사항??
){constructor():this("","",Timestamp.now())}

