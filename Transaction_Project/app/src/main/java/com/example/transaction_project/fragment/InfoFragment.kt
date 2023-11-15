package com.example.transaction_project.fragment


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.transaction_project.login.LoginActivity
import com.example.transaction_project.R
import com.example.transaction_project.home.Product
import com.example.transaction_project.login.UserInfo
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class InfoFragment : Fragment(R.layout.info_fragment) {


    private val db: FirebaseFirestore = Firebase.firestore
    private val userInfoCollectionRef = db.collection("UserInfo")


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMyPage(view)
        val logout = view.findViewById<Button>(R.id.logOutButton)

        logout?.setOnClickListener {
            doLogOut()
        }
        getUserInfo(view)
    }

    private fun getUserInfo(view: View) {
        val currentUser = Firebase.auth.currentUser
        currentUser?.let { user ->
            userInfoCollectionRef
                .whereEqualTo("email", user.email)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        val userInfo = documents.documents[0].toObject(UserInfo::class.java)
                        userInfo?.let {
                            val nameTextView = view.findViewById<TextView>(R.id.nameText)
                            nameTextView.text = it.name
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("InfoFragment", "Error getting user information", exception)
                }
        }
    }

    private fun initMyPage(view: View) {
        val favoriteList = view.findViewById<LinearLayout>(R.id.favorite_layout)
        favoriteList.setOnClickListener {
            Snackbar.make(view, "favo", Snackbar.LENGTH_SHORT).show()
        }
        val sellList = view.findViewById<LinearLayout>(R.id.sale_layout)
        sellList.setOnClickListener { Snackbar.make(view, "sell", Snackbar.LENGTH_SHORT).show() }
        val buyList = view.findViewById<LinearLayout>(R.id.pur_layout)
        buyList.setOnClickListener { Snackbar.make(view, "buy", Snackbar.LENGTH_SHORT).show() }
    }

    private fun doLogOut() {
        Firebase.auth.signOut()
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)


    }
}
