package com.example.transaction_project.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.transaction_project.R
import com.example.transaction_project.home.Product
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.util.Calendar
import java.util.TimeZone
import java.util.UUID


private val db: FirebaseFirestore = Firebase.firestore
val itemsCollectionRef = db.collection("Items")


data class Category (val name: String)

class WriteActivity : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var imageView: ImageView
    private lateinit var storageReference: StorageReference
    private var selectedImgUri : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.write_layout)
        setTitle("내 물건 팔기")

        // editText 바깥 클릭시 hide keyboard.
        val parentLayout = findViewById<View>(R.id.parentLayout)
        parentLayout.setOnTouchListener { v, event ->
            hideKeyboard()
            false
        };


        // 가격에 세자리마다 콤마 붙이기
        var write_price = findViewById<EditText>(R.id.write_price)
        write_price.addTextChangedListener(CustomTextWatcher(write_price))

        // 카테고리 만들기
        val categories = listOf(
            Category("디지털기기"),
            Category("생활가전"),
            Category("가공식품"),
            Category("생활/주방"),
            Category("게임/주변기기"),
            Category("문구"),
            Category("의류")

        )

        // 카테고리 버튼 -> 리사이클러뷰 지정
        val categoryBtn = findViewById<Button>(R.id.write_category)
        val recyclerView = findViewById<RecyclerView>(R.id.write_categoryItem)
        val adapter = CategoryAdapter(categories, categoryBtn, recyclerView)
        recyclerView.adapter = adapter

        adapter.setCategoryClickListener(object : CategoryAdapter.CategoryClickListener {
            override fun onCategoryClicked(categoryName: String) {
                // 카테고리 클릭 시 버튼 텍스트 변경
                categoryBtn.text = categoryName
            }
        })

        recyclerView.layoutManager = LinearLayoutManager(this)

        // 카테고리 항목 선택시 visible -> gone
        categoryBtn.setOnClickListener{
            if (recyclerView.visibility == View.VISIBLE) {
                recyclerView.visibility = View.GONE
            } else {
                recyclerView.visibility = View.VISIBLE
            }
        }

        // 스토리지 참조 및 이미지뷰.
        storageReference = FirebaseStorage.getInstance().reference
        imageView = findViewById(R.id.write_img)

        // 이미지 뷰 클릭시, 이미지 업로드 (저장 X)
        imageView.setOnClickListener {
            openGallery()
        }


        // 글 등록하기 버튼을 클릭 시, 데이터를 db에 넘겨주고 종료
        val write_compleBtn = findViewById<Button>(R.id.write_complete)
        write_compleBtn.setOnClickListener{
            pushItem()
        }

    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            selectedImgUri = data.data!!
            // 이미지뷰에 선택한 이미지 표시
            imageView.setImageURI(selectedImgUri)

        }
    }

    private fun uploadImage(imageUri: Uri) {
        val storageRef = storageReference.child("images/${UUID.randomUUID()}")
        val uploadTask = storageRef.putFile(imageUri)

        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            storageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                // 추후 쓰일 수 있음
            } else {

            }
        }
    }

    // 새 글 작성 창에서 -> DB로 데이터 삽입(만).
    private fun pushItem(){

        // 글 제목, 가격, 내용, 카테고리 -> 프래그먼트로 넘겨줄 것
        val title = findViewById<EditText>(R.id.write_editTitle).text.toString()
        val price = findViewById<EditText>(R.id.write_price).text.toString()
        val category = findViewById<Button>(R.id.write_category).text.toString()
        val detail = findViewById<EditText>(R.id.write_editDetail).text.toString()

        val now = Calendar.getInstance(
            TimeZone.getTimeZone("Asia/Seoul")).time


        // 이미지를 스토리지에 업로드
        selectedImgUri?.let { uploadImage(it) }
        val product = Product(title, selectedImgUri.toString(), "$price 원", now.time, "", detail, category)
        itemsCollectionRef
            .add(product)
            .addOnSuccessListener {
                Snackbar.make(findViewById<ConstraintLayout>(R.id.parentLayout),"정상적으로 등록되었습니다.",Snackbar.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                    exception ->
                Log.w("HomeFragment","Error adding documents: $exception")
            }
        try {
            Thread.sleep(2000) // 2초 동안 대기
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        finish()

    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_write, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId)  {

            // 뒤로 가기 버튼 클릭시
            R.id.action_back -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)

        }
    }


    // edittext 바깥쪽 클릭시 키보드 내리기
    fun hideKeyboard() {
        val inputManager = this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
            this.currentFocus!!.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }






}