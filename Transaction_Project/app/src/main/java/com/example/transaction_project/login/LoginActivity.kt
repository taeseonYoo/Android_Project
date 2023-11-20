package com.example.transaction_project.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.transaction_project.MainActivity
import com.example.transaction_project.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

//로그인 화면. 이메일과 비밀번호 입력 후 로그인 성공하면 home_fragment로 이동한다
//회원가입 버튼 누르면 회원가입 화면으로 이동.
class LoginActivity : AppCompatActivity() {

    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var logInButton: Button
    private lateinit var signUpButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.apply {
            hide()
        }
        email = findViewById(R.id.emailEditText)
        password = findViewById(R.id.passwordEditText)
        logInButton = findViewById(R.id.logInButton)
        signUpButton = findViewById(R.id.signUpButton1)

        logInButton.setOnClickListener {
            val email = email.text.toString()
            val password = password.text.toString()
            doLogin(email, password)
        }

        //회원가입 버튼 누르면 회원가입 창으로 이동
        signUpButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun doLogin(email: String, password: String) {
        Firebase.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { // it: Task<AuthResult!>
                if (it.isSuccessful) {
                    Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    Toast.makeText(this, "아이디와 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
                }
            }

    }
}
