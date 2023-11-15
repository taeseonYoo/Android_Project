package com.example.transaction_project.chat


import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.transaction_project.R
import com.example.transaction_project.home.ItemAdapter
import com.example.transaction_project.home.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text


class ChatTestActivity :AppCompatActivity() {

    private lateinit var item_img : ImageView
    private lateinit var item_title : TextView
    private lateinit var item_price : TextView
    private lateinit var user_name : Toolbar
    private lateinit var recyclerView : RecyclerView
    private lateinit var chatAdapter : ChatAdapter

    private val db: FirebaseFirestore = Firebase.firestore
    val chatList = arrayListOf<ChatListItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_test)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        recyclerView = findViewById<RecyclerView>(R.id.message)



        initItemAdapter()
        initField()
        setProfile()
        getChat()

    }

    //사용자 정보에 대한 값들을 저장하기 위한
    private fun initField(){
        item_img = findViewById<ImageView>(R.id.specific_img)
        item_title = findViewById<TextView>(R.id.specific_title)
        item_price = findViewById<TextView>(R.id.specific_price)
        user_name = findViewById<Toolbar>(R.id.toolbar)
    }

    private fun initItemAdapter(){
        chatAdapter = ChatAdapter(chatList)
        recyclerView.adapter = chatAdapter
        recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        chatAdapter.notifyDataSetChanged()
    }


    //DB->CHATLIST->USERID->CHAT에 있는 컬렉션을 가져온다.
    private fun getChat(){
        db.collection("/ChatList/userId/Chat").get()
            .addOnSuccessListener { result->
                chatList.clear()
                for(doc in result){
                    val message = ChatListItem(doc["userId"] as String,"",doc["message"] as String, doc["imgUrl"] as String, doc["checkRead"] as String)
                    chatList.add(message)
                }
                chatAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                    exception ->
                Log.w("ChatTestActivity", "Error getting documents: $exception")
            }

    }

    //물건의 정보를 세팅함. 가격 , 이름 , 이미지 , 사용자 이름
    private fun setProfile(){
        db.document("/ChatList/userId").get()
            .addOnSuccessListener {
                if(it["img"].toString() != ""){
                    Glide.with(item_img)
                        .load(it["img"].toString())
                        .error(R.drawable.user)
                        .into(item_img)
                }
                item_title.text = it["title"].toString()
                item_price.text = it["price"].toString()
                //상대 유저의 이름? 추후에 변경 예정
                user_name.title = it["userName"].toString()

            }
            .addOnFailureListener {
                    exception ->
                Log.w("ChatTestActivity", "Error getting documents: $exception")
            }
    }


}