package com.example.transaction_project

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

//로그인 화면. 이메일과 비밀번호 입력 후 로그인 성공하면 home_fragment로 이동한다
//회원가입 버튼 누르면 회원가입 화면으로 이동.
class LoginActivity : AppCompatActivity() {

    // emailEditText,passwordEditText,logInButton,singUpButton
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var logInButton: Button
    private lateinit var signUpButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        logInButton = findViewById(R.id.logInButton)
        signUpButton = findViewById(R.id.singUpButton)

        logInButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()


            if (email.isNotEmpty() && password.isNotEmpty()) {
                Firebase.auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {   //로그인 성공시 -> 홈화면으로 이동

                            Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show()
                            //findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                        } else {
                            // Login failed
                            Toast.makeText(this, "아이디와 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "아이디와 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }


        //회원가입 버튼 누르면 회원가입 창으로 이동
        signUpButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

    }
}