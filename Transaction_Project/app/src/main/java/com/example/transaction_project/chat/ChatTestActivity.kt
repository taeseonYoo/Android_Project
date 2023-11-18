package com.example.transaction_project.chat
import com.example.transaction_project.chat.ChatAdapter
import com.example.transaction_project.chat.ChatListItem



import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.transaction_project.R
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
//아직 .. 미완성

class ChatTestActivity :AppCompatActivity() {

    private lateinit var item_img : ImageView
    private lateinit var item_title : TextView
    private lateinit var item_price : TextView
    private lateinit var user_name : Toolbar
    private lateinit var recyclerView : RecyclerView
    private lateinit var chatAdapter : ChatAdapter
    private lateinit var itemId : String
    private lateinit var chatRoomId: String

    private val db: FirebaseFirestore = Firebase.firestore
    val chatList = arrayListOf<ChatListItem>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_test)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        //인텐트로 아이템 아이디 가져오는 코드
        val intent = getIntent();
        if (intent != null) {
            itemId = intent.getStringExtra("itemId").toString()
            chatRoomId = intent.getStringExtra("chatRoomId").toString()
            println(itemId)
        }
        //인텐트 가져오는 코드 끝

        initResource()
        initItemAdapter()
        initSendMessage()
        initProfile()
        initMessage()

    }

    //사용자 정보에 대한 값들을 저장하기 위하여 리소스 연결
    private fun initResource(){
        recyclerView = findViewById<RecyclerView>(R.id.message)
        item_img = findViewById<ImageView>(R.id.specific_img)
        item_title = findViewById<TextView>(R.id.specific_title)
        item_price = findViewById<TextView>(R.id.specific_price)
        user_name = findViewById<Toolbar>(R.id.toolbar)
    }

    private fun initItemAdapter(){
        chatAdapter = ChatAdapter(chatList)
        recyclerView.adapter = chatAdapter
        recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
    }


    private fun initSendMessage(){
        val msgInput = findViewById<EditText>(R.id.wt_chat)
        val sendMsg = findViewById<ImageButton>(R.id.sd_chat)

        sendMsg.setOnClickListener {
            if (msgInput.text.toString() != "") {
                val tmp = ChatListItem("2", "1", msgInput.text.toString(), Timestamp.now())
                db.collection("ChatRoom").document(chatRoomId).collection("Chat").add(tmp)
                    .addOnSuccessListener {
                        msgInput.setText("")
                        chatList.add(tmp)
                        chatAdapter.notifyDataSetChanged()
                        loadItemDetails() // 추가: 새로운 메시지 추가 후 아이템 정보 로드
                    }
                    .addOnFailureListener { exception ->
                        Log.w("ChatTestActivity", "Error getting documents: $exception")
                    }
            }
        }
    }


    //DB->CHATLIST->USERID->CHAT에 있는 컬렉션을 가져온다.

    private fun initMessage(){
        db.collection("ChatRoom").document(chatRoomId).collection("Chat")
            .orderBy("currentDate", Query.Direction.ASCENDING).get()
            .addOnSuccessListener { result ->
                chatList.clear()
                for (doc in result) {
                    val userId = doc["userId"] as? String ?: ""
                    val message = ChatListItem(
                        userId, "", doc["message"] as? String ?: "",
                        doc["currentDate"] as? Timestamp ?: Timestamp.now()
                    )
                    chatList.add(message)
                }
                chatAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.w("ChatTestActivity", "Error getting documents: $exception")
            }
    }

    private fun initProfile() {
        db.collection("ProductList")
            .whereEqualTo("itemId", itemId)
            .get()
            .addOnSuccessListener {
                for (doc in it) {
                    if (doc["imgUrl"].toString() != "") {
                        Glide.with(item_img)
                            .load(doc["imgUrl"].toString())
                            .error(R.drawable.user)
                            .into(item_img)
                    }
                    item_title.text = doc["title"].toString()
                    item_price.text = doc["price"].toString()
                    user_name.title = doc["sellerId"].toString()
                }
            }
            .addOnFailureListener { exception ->
                Log.w("ChatTestActivity", "Error getting documents: $exception")
            }
    }

    //수정
    private fun loadItemDetails() {
        db.collection("ProductList").document(itemId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val imgUrl = document["imgUrl"].toString()
                    val price = document["price"].toString()
                    val title = document["title"].toString()

                    for (item in chatList) {
                        item.itemImgUrl = imgUrl
                        item.itemPrice = price
                        item.itemTitle = title
                    }

                    chatAdapter.notifyDataSetChanged()
                } else {
                    Log.d("ChatTestActivity", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("ChatTestActivity", "get failed with ", exception)
            }
    }




}


