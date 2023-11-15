package com.example.transaction_project.login

import com.google.firebase.firestore.QueryDocumentSnapshot
//DB 사용자 정보. 비밀번호는 생략한다
data class UserInfo (
    val name : String,
    val birthYear : String,
    val birthMonth : String,
    val birthDay : String,
    val email : String
){
    constructor():this("","","","","")
}
