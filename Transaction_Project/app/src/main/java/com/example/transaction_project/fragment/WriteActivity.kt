package com.example.transaction_project.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.transaction_project.MyViewModel
import com.example.transaction_project.R



data class Category (val name: String)

class WriteActivity : AppCompatActivity() {

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



        // 글 등록하기 버튼을 클릭 시, bundle 데이터를 가지고 홈 프래그먼트로 이동
        val write_compleBtn = findViewById<Button>(R.id.write_complete)
        write_compleBtn.setOnClickListener{
            write_to_home()
        }

    }

    private fun write_to_home(){



        // 글 제목, 가격, 내용, 카테고리 -> 프래그먼트로 넘겨줄 것
        val title = findViewById<EditText>(R.id.write_editTitle).text.toString()
        val price = findViewById<EditText>(R.id.write_price).text.toString()
        val category = findViewById<Button>(R.id.write_category).text.toString()
        val detail = findViewById<EditText>(R.id.write_editDetail).text.toString()

        // 데이터를 번들로 패키징
        val sharedPreferences = getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("title", title)
        editor.putString("price", price)
        editor.putString("category", category)
        editor.putString("detail", detail)
        editor.apply()

        finish()

        Log.d("WriteActivity", "This is write activity:" + sharedPreferences.getString("title", ""))

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