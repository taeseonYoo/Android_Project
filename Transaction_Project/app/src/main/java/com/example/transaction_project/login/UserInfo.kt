package com.example.transaction_project.login

import com.google.firebase.firestore.QueryDocumentSnapshot
//DB 사용자 정보. 비밀번호는 생략한다
data class UserInfo (
    val name : String,
    val birth : String,
    val email : String
){
    constructor():this("","","")
}
