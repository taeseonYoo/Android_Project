package com.example.transaction_project


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.transaction_project.fragment.ChatFragment

import com.example.transaction_project.fragment.HomeFragment
import com.example.transaction_project.fragment.InfoFragment
import com.example.transaction_project.login.LoginActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTitle("중고 마켓")

        //로그인 되어있는지 확인 -> 되어있으면 메인화면, 아니면 로그인화면
        if (Firebase.auth.currentUser != null) {
            showMainScreen()

        } else {
            showLogin()
        }
    }

    //로그인 화면 보여주기
    private fun showLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    //메인 화면 보여주기
    private fun showMainScreen() {
        setTitle("중고 마켓")

        val homeFragment = HomeFragment()
        val chatFragment = ChatFragment()
        val infoFragment = InfoFragment()

        //첫 프래그먼트를 homeFragment로 지정
        changeFragment(homeFragment)

        val navigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        //하단 네비게이션 뷰 클릭 이벤트로 프래그먼트 교체
        navigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> changeFragment(homeFragment)
                R.id.chat -> changeFragment(chatFragment)
                R.id.auth -> changeFragment(infoFragment)
            }
            true
        }
    }

    //프래그먼트 변경
    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fragment, fragment)
        }
    }

}