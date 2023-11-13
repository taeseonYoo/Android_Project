package com.example.transaction_project.chat

//채팅창 목록 데이터 담는 객체
data class ChatRoom(
    val sellerId : String,//상대방 이름
    val message : String,
    val imgUrl : String,
    val checkRead : String //채팅 읽었는지 안 읽었는지
    //val itemName : String
    //추가 할 사항??
){constructor():this("","","", "")}
//checkRead 디폴트값을 0으로 해서
//채팅목록에서는 sellerID, message, imgUrl, checkRead만 일단 필요할듯.
//아직 미완성. 수정 필요
//firestore 어떻게 구성할지 회의해보고 다시 고치기
