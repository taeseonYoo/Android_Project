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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date


class ProductDetailActivity : AppCompatActivity() {
    private var isHeartClicked =false;

    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val currentUserUid: String = auth.currentUser.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        setTitle("중고 마켓")

        getAndSetData()
        messageAndCloseFunction()
        makeHeart()



    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {


       /* db.collection("items")
            .document(currentUserUid) // 여기서 currentUserUid는 사용자의 UID로 가정
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // writer 필드 값 가져오기
                    val writerFieldValue = document.getString("writer") // 현재 로그인 한 사람의 writer 필드 값.
                    if(writerFieldValue==intent.getStringExtra("writer")){  //현재 로그인 한 사람의 writer필드 값과 접속 게시물의 writer 필드 값 비교


                    }
                } else {
                    // 도큐먼트가 존재하지 않을 때
                    println("도큐먼트 존재하지 않음")

                }
            }
            .addOnFailureListener { e ->
                // 실패 시 null
                println("사용자의 도큐먼트를 불러오는 도중 오류 발생: $e")
            }*/
        menuInflater.inflate(R.menu.product_menu_option, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId) {
            R.id.menu_item_edit -> {
                showConfirmationDialogModify()
            }
            R.id.menu_item_delete -> {
                showConfirmationDialogDelete()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    /*fun modifyMyPost(){

    }*/

    private fun showConfirmationDialogModify() {
        val alertDialogBuilder = AlertDialog.Builder(this)

        alertDialogBuilder.setTitle("글 수정 다이얼로그")
        alertDialogBuilder.setMessage("계속하시겠습니까?")

        // "확인" 버튼 설정 및 이벤트 처리
        alertDialogBuilder.setPositiveButton("확인") { dialogInterface: DialogInterface, _: Int ->
            // 확인 버튼이 눌렸을 때
            //var modifyFragment = modifyFragment()
            //modifyMyPost()


            dialogInterface.dismiss() // 다이얼로그 닫기 (선택사항)
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

    /*fun deleteMyPost() {


        // "items" 컬렉션에서 도큐먼트 삭제
        db.collection("items")
            .document(currentUserUid)
            .delete()
            .addOnSuccessListener {
                // 삭제 성공 시 실행할 코드
                println("게시물이 성공적으로 삭제되었습니다.")
            }
            .addOnFailureListener { e ->
                // 삭제 실패 시 실행할 코드
                println("게시물 삭제 중 오류 발생: $e")
            }
    }*/

    private fun showConfirmationDialogDelete() {
        val alertDialogBuilder = AlertDialog.Builder(this)



        alertDialogBuilder.setTitle("글 삭제 다이얼로그")
        alertDialogBuilder.setMessage("계속하시겠습니까?")

        // "확인" 버튼 설정 및 이벤트 처리
        alertDialogBuilder.setPositiveButton("확인") { dialogInterface: DialogInterface, _: Int ->
            // 확인 버튼이 눌렸을 때
            //var deleteFragment = deleteFragment()

            //deleteMyPost()
            dialogInterface.dismiss() // 다이얼로그 닫기 (선택사항)
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

    fun getAndSetData(){
        // Intent로부터 정보를 가져옵니다.
        val title = intent.getStringExtra("title")
        val imgUrl = intent.getStringExtra("imgUrl")
        val price = intent.getStringExtra("price")
        val time = intent.getLongExtra("time", 0)
        val writer = intent.getStringExtra("writer")
        val content = intent.getStringExtra("content")
        val status = intent.getStringExtra("status")
        val category = intent.getStringExtra("category")

        // 가져온 정보를 레이아웃의 텍스트뷰 등을 사용하여 표시합니다.
        val imgUrlImageView = findViewById<ImageView>(R.id.imgUrlImageView)
        val titleTextView = findViewById<TextView>(R.id.titleTextView)
        val priceTextView = findViewById<TextView>(R.id.priceTextView)
        val timeTextView = findViewById<TextView>(R.id.timeTextView)
        val contentTextView = findViewById<TextView>(R.id.contentTextView)
        val statusTextView = findViewById<TextView>(R.id.statusTextView)
        val writerTextView = findViewById<TextView>(R.id.writerTextView)
        val categoryTextView = findViewById<TextView>(R.id.categoryTextView)


        //값 받아와서 layout 구성
        titleTextView.text = title
        Glide.with(this)
            .load(imgUrl)
            .into(imgUrlImageView)
        priceTextView.text = price
        if (content != null) {
            if(content.isNotEmpty()){
                contentTextView.text = content
                contentTextView.setBackgroundColor(Color.parseColor("#FFE0B2"))
            }
        }
        //contentTextView.text = content
        val format = SimpleDateFormat("MM월 dd일")
        val date = Date(time)
        timeTextView.text = format.format(date).toString()
        writerTextView.text = writer
        statusTextView.text = status
        categoryTextView.text = category

        //status에 따른 처리
        when(status){
            "reserve"->{ //예약중
                statusTextView.text = "예약중"
                statusTextView.setBackgroundResource(R.drawable.reserve_status)
            }
            "complete"->{ //거래완료
                statusTextView.text = "거래완료"
                statusTextView.setBackgroundResource(R.drawable.complete_status)
            }
            "onSale"-> { //판매중
                statusTextView.text = "판매중"
                statusTextView.setBackgroundResource(R.drawable.onsale_status)
            }

        }
    }

    fun messageAndCloseFunction(){
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


    fun makeHeart(){
        val heartButton = findViewById<ImageView>(R.id.heartButton)

        heartButton.setOnClickListener {
            toggleHeartState() // 하트 상태를 토글하는 메소드 호출
        }
    }

    private fun changeFragment(fragment: Fragment){
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

