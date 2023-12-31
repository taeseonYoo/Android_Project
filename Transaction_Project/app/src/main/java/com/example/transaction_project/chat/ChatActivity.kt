package com.example.transaction_project.chat


import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.transaction_project.FirestoreInstance
import com.example.transaction_project.R
import com.google.firebase.Timestamp
import com.google.firebase.database.collection.LLRBNode
import com.google.firebase.firestore.Query

//채팅방
class ChatActivity :AppCompatActivity() {

    private lateinit var item_img : ImageView
    private lateinit var item_title : TextView
    private lateinit var item_price : TextView
    private lateinit var user_name : Toolbar
    private lateinit var recyclerView : RecyclerView
    private lateinit var chatAdapter : ChatAdapter
    private lateinit var productId : String
    private lateinit var documentId : String
    private lateinit var otherUserName : String

    val chatList = arrayListOf<ChatListItem>()
    val uid = FirestoreInstance.auth.currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_detail)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            deleteChatRoom()
        }

        //인텐트로 아이템 아이디 가져오는 코드
        val intent = getIntent();
        if(intent != null){
            productId = intent.getStringExtra("productId").toString()
            documentId = intent.getStringExtra("chatRoomId").toString()
            otherUserName = intent.getStringExtra("otherUserName").toString()
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
            if(msgInput.text.toString() != ""){
                val tmp = ChatListItem(uid.toString(), msgInput.text.toString() , Timestamp.now())
                FirestoreInstance.chatRoomListRef
                    .document(documentId)
                    .collection("/Chat")
                    .add(tmp)
                    .addOnSuccessListener {
                        msgInput.setText("")
                        //새로운 메세지를 확인 하는 두 가지 방식 fs. chatlist에 추가하고 변경을 알려 초기화 sd. initMessage 다시 한 번 데이터 가져오기.
                        chatList.add(tmp)  // 이 방식을 이용하면 버그가 발생 할 것 같음.
                        chatAdapter.notifyDataSetChanged()
                        //InitMessage()
                    }
                    .addOnFailureListener {
                            exception ->
                        Log.w("ChatActivity", "Error getting documents: $exception")
                    }
            }
        }
    }


    //DB->CHATLIST->USERID->CHAT에 있는 컬렉션을 가져온다.

    private fun initMessage(){
        FirestoreInstance.chatRoomListRef
            .document(documentId)
            .collection("/Chat")
            .orderBy("timeAt",Query.Direction.ASCENDING).get() //timeAt을 기준으로 오름차순해서 메세지를 가져옵니다.
            .addOnSuccessListener { result->
                chatList.clear()
                for(doc in result){ //작성자 uid 가져온 것
                    val message = ChatListItem(doc["uid"] as String,doc["message"] as String, doc["timeAt"] as Timestamp)
                    chatList.add(message)
                }
                chatAdapter.notifyDataSetChanged()

                //제일 최근 아이템이 보이도록 스크롤위치조정
                val lastItemPosition = chatAdapter.itemCount - 1
                if (lastItemPosition >= 0) {
                    recyclerView.scrollToPosition(lastItemPosition)
                }
            }
            .addOnFailureListener {
                    exception ->
                Log.w("ChatActivity", "Error getting documents: $exception")
            }

    }

    //인텐트로 받아 온 productId로 상품의 데이터를 설정한다.
    private fun initProfile(){
        FirestoreInstance.itemListRef
            .whereEqualTo("productId" , productId)
            .get()
            .addOnSuccessListener {

                if(it.isEmpty){
                    item_title.text = "게시글이 삭제 되었습니다."
                    item_title.setTextColor(Color.RED)
                    item_price.text = ""
                    user_name.title = ""
                    item_img.setImageResource(R.drawable.user)
                    findViewById<ImageButton>(R.id.sd_chat).isEnabled = false
                    findViewById<EditText>(R.id.wt_chat).isEnabled = false
                }

                for(doc in it){

                    if(doc["imgUrl"].toString() != ""){
                        Glide.with(item_img)
                            .load(doc["imgUrl"].toString())
                            .error(R.drawable.user)
                            .into(item_img)
                    }
                    val tmp = doc["price"].toString() + "원"
                    item_title.text = doc["title"].toString()
                    item_price.text = tmp
                    //상대 유저의 이름? 추후에 변경 예정 , 필드 명도 변경 해야함
                    user_name.title = otherUserName

                }
            }
            .addOnFailureListener {
                    exception ->
                Log.w("ChatActivity", "Error getting documents: $exception")
            }
    }

//    private fun findUserName(otherUID : String){
//        FirestoreInstance.userInfoRef.document(otherUID)
//            .get()
//            .addOnSuccessListener {
//                user_name.title = it["name"] as? String
//
//            }
//            .addOnFailureListener {
//
//            }
//        chatAdapter.notifyDataSetChanged()
//    }


    private fun deleteChatRoom() {  //메세지 없으면 DB에서 삭제시킴
        FirestoreInstance.chatRoomListRef
            .document(documentId)
            .collection("/Chat")
            .get()
            .addOnSuccessListener { message ->
                if (message.isEmpty) {
                    FirestoreInstance.chatRoomListRef
                        .document(documentId)
                        .delete()
                        .addOnSuccessListener {
                            finish()
                        }
                } else {
                    finish()
                }
            }
    }

}
