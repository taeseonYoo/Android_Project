package com.example.transaction_project.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.transaction_project.R
import com.example.transaction_project.chat.ChatTestActivity

class ChatFragment :Fragment(R.layout.chat_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.test).setOnClickListener {
            val intent = Intent(requireContext(), ChatTestActivity::class.java)
            startActivity(intent)
        }
    }

}