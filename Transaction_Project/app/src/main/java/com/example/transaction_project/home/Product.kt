package com.example.transaction_project.home

import com.google.firebase.firestore.QueryDocumentSnapshot

data class Product(
    val title : String,
    val imgUrl : String,
    val price : String,
    val time : Long, //테스트를 위해 String 타입으로 설정, 차후에 변환해야할듯
    val status : String, // 예약중, 판매완료 등
    val detail: String, // 글 내용
    val category: String
){ // for FireStore
    constructor():this("","","",0,"", "", "")
}
