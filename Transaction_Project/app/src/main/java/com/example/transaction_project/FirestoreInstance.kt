package com.example.transaction_project

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object FirestoreInstance {
    val db: FirebaseFirestore by lazy {Firebase.firestore}
    val auth: FirebaseAuth by lazy {Firebase.auth}
    val userInfoRef by lazy {db.collection("UserInfo")} //사용자 정보 컬렉션
    val chatRoomListRef by lazy { db.collection("/ChatRoomList")}
    val itemListRef by lazy {db.collection("Items")}
}