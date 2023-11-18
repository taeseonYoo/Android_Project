package com.example.transaction_project.chat

import com.google.firebase.Timestamp
import java.util.Date

//채팅창 목록 데이터 담는 객체
data class ChatRoom(
    val chatRoomId: String,
    val otherUserName: String,
    var lastMessage: String, //없애도 될거같음
    val currentDate: Timestamp,
    val imgUrl : String
){constructor():this("", "","",Timestamp.now(),"")}

