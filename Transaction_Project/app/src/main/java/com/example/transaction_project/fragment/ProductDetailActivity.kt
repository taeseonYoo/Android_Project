package com.example.transaction_project.fragment


import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.bumptech.glide.Glide
import com.example.transaction_project.MainActivity
import com.example.transaction_project.R
import com.example.transaction_project.home.ItemAdapter
import com.example.transaction_project.home.Product
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class ProductDetailActivity : AppCompatActivity() {
    private var isHeartClicked = false;

    private val db: FirebaseFirestore = Firebase.firestore
    val itemsCollectionRef = db.collection("Items")
    val usersCollectionRef = db.collection("UserInfo")
    private lateinit var productId: String
    private lateinit var uid: String




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_product_detail)
        setTitle("중고 마켓")
        messageAndCloseFunction()
        makeHeart()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.product_menu_option, menu)

        getAndSetData(menu)
        return true
    }


    // 글 작성자와 일치하는지 확인하기
    private fun checkPermission(): Boolean {

        val currentUser = Firebase.auth.currentUser
        return currentUser != null && uid == currentUser.uid
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        when (item?.itemId) {
            R.id.menu_item_edit -> {
                showConfirmationDialogModify()
            }

            R.id.menu_item_delete -> {
                showConfirmationDialogDelete()
            }
        }

        return super.onOptionsItemSelected(item)
    }





    private fun showConfirmationDialogModify() {
        val alertDialogBuilder = AlertDialog.Builder(this)

        alertDialogBuilder.setTitle("글 수정 다이얼로그")
        alertDialogBuilder.setMessage("계속하시겠습니까?")

        // "확인" 버튼 설정 및 이벤트 처리
        alertDialogBuilder.setPositiveButton("확인") { dialogInterface: DialogInterface, _: Int ->
            // 확인 버튼이 눌렸을 때
            //var modifyFragment = modifyFragment()

            val intent = Intent(this, WriteActivity::class.java)
            intent.putExtra("modify",true)
            intent.putExtra("productId",productId)
            startActivity(intent)
            finish()



        }

        // "취소" 버튼 설정 및 이벤트 처리
        alertDialogBuilder.setNegativeButton("취소") { dialogInterface: DialogInterface, _: Int ->
            // 취소 버튼이 눌렸을 때
            dialogInterface.dismiss() // 다이얼로그 닫기 (선택사항)
        }

        val alertDialog = alertDialogBuilder.create()

        // 다이얼로그를 화면에 표시
        alertDialog.show()
    }

    fun deleteMyPost() {

        itemsCollectionRef
            .whereEqualTo("productId", productId)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    // 만약 해당 id에 해당하는 문서가 없다면.
                    return@addOnSuccessListener

                }
                for (document in documents) {
                    // 각 문서에 대한 참조를 얻어와서 삭제
                    val documentId = document.id
                    val documentReference = itemsCollectionRef.document(documentId)
                    documentReference
                        .delete()
                        .addOnSuccessListener {
                            // 삭제 성공
                            println("문서 삭제 완료: $documentId")
                            Thread.sleep(2000)
                        }
                        .addOnFailureListener { e ->
                            // 삭제 실패
                            println("문서 삭제 실패: $documentId, 오류: $e")
                        }
                }


            }
    }


    private fun showConfirmationDialogDelete() {
        val alertDialogBuilder = AlertDialog.Builder(this)



        alertDialogBuilder.setTitle("글 삭제 다이얼로그")
        alertDialogBuilder.setMessage("계속하시겠습니까?")

        // "확인" 버튼 설정 및 이벤트 처리
        alertDialogBuilder.setPositiveButton("확인") { dialogInterface: DialogInterface, _: Int ->
            // 글 삭제처리
            deleteMyPost()
            finish()
        }

        // "취소" 버튼 설정 및 이벤트 처리
        alertDialogBuilder.setNegativeButton("취소") { dialogInterface: DialogInterface, _: Int ->
            // 취소 버튼이 눌렸을 때
            dialogInterface.dismiss() // 다이얼로그 닫기 (선택사항)
        }

        val alertDialog = alertDialogBuilder.create()

        // 다이얼로그를 화면에 표시
        alertDialog.show()
    }


    fun getAndSetData(menu: Menu?) {
        productId = intent.getStringExtra("productId").toString()
        itemsCollectionRef
            .whereEqualTo("productId", productId)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    // 만약 해당 id에 해당하는 문서가 없다면.
                    return@addOnSuccessListener
                }
                val item = documents.documents[0]
                uid = item.getString("sellerId").toString()

                val title = item.getString("title")
                val price = item.getString("price")
                val category = item.getString("category")
                val detail = item.getString("detail")
                val time = item.getTimestamp("crDate")
                val status = item.getString("status")
                val imgUrl = item.getString("imgUrl")



                if (uid != null) {
                    // 가져온 정보를 레이아웃의 텍스트뷰 등을 사용하여 표시합니다.
                    val imgUrlImageView = findViewById<ImageView>(R.id.imgUrlImageView)
                    val titleTextView = findViewById<TextView>(R.id.titleTextView)
                    val priceTextView = findViewById<TextView>(R.id.priceTextView)
                    val timeTextView = findViewById<TextView>(R.id.timeTextView)
                    val contentTextView = findViewById<TextView>(R.id.contentTextView)
                    val statusTextView = findViewById<TextView>(R.id.statusTextView)
                    val writerTextView = findViewById<TextView>(R.id.writerTextView)
                    val categoryTextView = findViewById<TextView>(R.id.categoryTextView)

                    Glide.with(imgUrlImageView)
                        .load(imgUrl)
                        .skipMemoryCache(true)
                        .error(R.drawable.user)
                        .into(imgUrlImageView)


                    contentTextView.setBackgroundColor(Color.parseColor("#FFE0B2"))

                    val format = SimpleDateFormat("MM월 dd일", Locale.getDefault())
                    val date = format.format(time?.toDate())

                    titleTextView.text = title
                    priceTextView.text = "${price} 원"
                    contentTextView.text = detail
                    timeTextView.text = date.toString()
                    categoryTextView.text = category

                    var writer: String
                    val user = usersCollectionRef.document(uid)
                    user.get()
                        .addOnSuccessListener { documentSnapshot ->
                            if (documentSnapshot.exists()) {
                                writer = documentSnapshot.getString("name").toString()
                                writerTextView.text = writer
                            }
                        }

                    statusTextView.text = status


                }
                val delete = menu?.findItem(R.id.menu_item_delete)
                val edit = menu?.findItem(R.id.menu_item_edit)
                if (!checkPermission()) {
                    delete?.isVisible = false
                    edit?.isVisible = false
                }
            }
    }

    fun messageAndCloseFunction() {
        // 메세지 버튼 기능 추가
        val message = findViewById<Button>(R.id.messageButton)
        message.setOnClickListener {
            /*val changeToMessage = messageFragment()
            changeFragment(changeToMessage)*/
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } //message창 만들고.

        //close 버튼 기능 추가
        val close = findViewById<Button>(R.id.closeButton)
        close.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }


    fun makeHeart() {
        val heartButton = findViewById<ImageView>(R.id.heartButton)

        heartButton.setOnClickListener {
            toggleHeartState() // 하트 상태를 토글하는 메소드 호출
        }
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fragment, fragment)
        }
    }

    // 클릭 시 하트 상태를 토글하는 메소드
    private fun toggleHeartState() {
        val heartButton = findViewById<ImageView>(com.example.transaction_project.R.id.heartButton)
        if (isHeartClicked) {
            // 하트가 켜진 상태에서 클릭되었을 때
            heartButton.setImageResource(com.example.transaction_project.R.drawable.ic_heart_off) // 꺼진 상태 이미지로 변경
        } else {
            // 하트가 꺼진 상태에서 클릭되었을 때
            heartButton.setImageResource(com.example.transaction_project.R.drawable.ic_heart_on) // 켜진 상태 이미지로 변경
        }
        isHeartClicked = !isHeartClicked // 상태를 토글
    }
}