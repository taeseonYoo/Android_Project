package com.example.transaction_project.login


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.transaction_project.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Locale


//회원가입 화면. 회원가입 후에는 자동으로 로그인 화면으로 다시 넘어가게 된다. DB에도 정상적으로 값 삽입
class SignUpActivity  : AppCompatActivity() {


    private lateinit var signUpEmailEditText: EditText
    private lateinit var signUpPasswordEditText: EditText
    private lateinit var signUpYearEditText: EditText
    private lateinit var signUpMonthEditText: EditText
    private lateinit var signUpDayEditText: EditText
    private lateinit var signUpNameEditText: EditText
    private lateinit var signUpButton2: Button

    private val db: FirebaseFirestore = Firebase.firestore
    private val userInfoCollectionRef = db.collection("UserInfo")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        signUpYearEditText = findViewById(R.id.signUpYearEditText)
        signUpMonthEditText = findViewById(R.id.signUpMonthEditText)
        signUpDayEditText = findViewById(R.id.signUpDayEditText)
        signUpNameEditText = findViewById(R.id.signUpNameEditText)
        signUpEmailEditText = findViewById(R.id.signUpEmailEditText)
        signUpPasswordEditText = findViewById(R.id.signUpPasswordEditText)
        signUpButton2 = findViewById(R.id.signUpButton2)

        //signUpBirthEditText.setText(SimpleDateFormat("YYYY/MM/dd", Locale.getDefault()).format(currentTime))
        signUpButton2.setOnClickListener {

            val name = signUpNameEditText.text.toString()
            val year = signUpYearEditText.text.toString()
            val month = signUpMonthEditText.text.toString()
            val day = signUpDayEditText.text.toString()
            val email = signUpEmailEditText.text.toString()
            val password = signUpPasswordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                Firebase.auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) {

                        if (it.isSuccessful) { //회원가입 성공시. db에 데이터 삽입 후 화면 전환
                            Toast.makeText(this, "회원가입 성공!", Toast.LENGTH_SHORT).show()
                            //val test = UserInfo("dms", "20202020", "999@naver.com")
                            val userMap = hashMapOf(
                                "name" to name,
                                "year" to year,
                                "month" to month,
                                "day" to day,
                                "email" to email
                            )

                            userInfoCollectionRef
                                .add(userMap)
                                .addOnSuccessListener {
                                    val intent = Intent(this, LoginActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                .addOnFailureListener {
                                }


                        } else { //회원가입 실패시
                            Toast.makeText(this, "회원가입 실패. 아이디와 비밀번호를 확인해주세요", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

            }
        }
    }
}


/* 수정전 코드. 일단 삭제x
private lateinit var signUpEmailEditText: EditText
private lateinit var signUpPasswordEditText: EditText
private lateinit var signUpBirthEditText: EditText
private lateinit var signUpNameEditText: EditText
private lateinit var signUpButton2: Button

private val db: FirebaseFirestore = Firebase.firestore
private val userInfoCollectionRef = db.collection("UserInfo")

val itemList = arrayListOf<Product>()

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_signup)

    signUpEmailEditText = findViewById(R.id.signUpEmailEditText)
    signUpPasswordEditText = findViewById(R.id.signUpPasswordEditText)
    signUpButton2 = findViewById(R.id.signUpButton2)


    signUpButton2.setOnClickListener {

        val email = signUpEmailEditText.text.toString()
        val password = signUpPasswordEditText.text.toString()
        doSignUp(email, password)

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()

    }
}

private fun doSignUp(email: String, password: String) {
    if (email.isNotEmpty() && password.isNotEmpty()) {
        Firebase.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {

                if (it.isSuccessful) {

                    Toast.makeText(this, "회원가입 성공!", Toast.LENGTH_SHORT).show()
                    pushUserInfo()
                } else {
                    Toast.makeText(this, "회원가입 실패. 아이디와 비밀번호를 확인해주세요", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    } else {
        Toast.makeText(this, "아이디와 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
    }
}




//데이터베이스에 값 삽입
private fun pushUserInfo(){
    val name= signUpNameEditText.text.toString()
    val birth = signUpBirthEditText.text.toString()
    val email = signUpEmailEditText.text.toString()

    val test = UserInfo("dms","20202020","999@naver.com")
    val userMap = hashMapOf(
        "name" to name,
        "birth" to birth,
        "email" to email
    )

    userInfoCollectionRef
        .add(test)
        .addOnSuccessListener {
            Toast.makeText(this, "db 등록 성공!", Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener {
            Toast.makeText(this, "db 등록 실패!", Toast.LENGTH_SHORT).show()
        }
    //itemAdapter.notifyDataSetChanged()
}



}

*/
