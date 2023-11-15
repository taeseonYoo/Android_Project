package com.example.transaction_project.fragment


import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.transaction_project.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class InfoFragment : Fragment(R.layout.info_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMyPage(view)
        val login = view.findViewById<Button>(R.id.signUpButton)

        login?.setOnClickListener {
            val userEmail = view.findViewById<EditText>(R.id.emailEditText)?.text.toString()
            val password = view.findViewById<EditText>(R.id.passwordEditText)?.text.toString()
            doLogin(userEmail, password)
        }
    }

    private fun doLogin(userEmail: String, password:String){
        Firebase.auth.signInWithEmailAndPassword(userEmail, password)
            .addOnCompleteListener(requireActivity()) { // it: Task<AuthResult!>
                if (it.isSuccessful) {
                    Snackbar.make(requireView(),"Authentication success",Snackbar.LENGTH_SHORT).show()

                } else {
                    Snackbar.make(requireView(), "Authentication failed.", Snackbar.LENGTH_SHORT).show()
                }
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

}