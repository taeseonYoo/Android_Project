package com.example.transaction_project.fragment


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.transaction_project.LoginActivity
import com.example.transaction_project.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class InfoFragment : Fragment(R.layout.info_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMyPage(view)
        val logout = view.findViewById<Button>(R.id.logOutButton)

        logout?.setOnClickListener {
            doLogOut()
        }
    }

    private fun initMyPage(view: View) {
        val favoriteList = view.findViewById<LinearLayout>(R.id.favorite_layout)
        favoriteList.setOnClickListener { Snackbar.make(view,"favo", Snackbar.LENGTH_SHORT).show() }
        val sellList = view.findViewById<LinearLayout>(R.id.sale_layout)
        sellList.setOnClickListener { Snackbar.make(view,"sell", Snackbar.LENGTH_SHORT).show() }
        val buyList = view.findViewById<LinearLayout>(R.id.pur_layout)
        buyList.setOnClickListener { Snackbar.make(view,"buy", Snackbar.LENGTH_SHORT).show() }
    }

    private fun doLogOut(){
        Firebase.auth.signOut()
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)


    }
}