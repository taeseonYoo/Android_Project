package com.example.transaction_project

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

//회원가입 화면. 회원가입 후에는 자동으로 로그인 화면으로 다시 넘어가게 된다
class SignUpActivity  : AppCompatActivity() {

    // emailEditText,passwordEditText,logInButton,singUpButton
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signUpButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        signUpButton = findViewById(R.id.singUpButton)


        signUpButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                Firebase.auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        //회원가입 성공하면 로그인 화면으로 다시 이동
                        if (task.isSuccessful) {
                            Toast.makeText(this, "회원가입 성공!", Toast.LENGTH_SHORT).show()
                            //val intent = Intent(this, LoginActivity::class.java)
                            //startActivity(intent)
                        } else { //회원가입 실패시. ex)이미 있는 계정인 경우
                            Toast.makeText(this, "회원가입 실패. 아이디와 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {    //이메일, 비밀번호 입력이 제대로 되어있지 않을 경우
                Toast.makeText(this, "아이디와 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

}