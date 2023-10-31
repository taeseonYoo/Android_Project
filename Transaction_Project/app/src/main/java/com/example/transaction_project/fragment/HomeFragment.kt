package com.example.transaction_project.fragment

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.transaction_project.R
import com.example.transaction_project.home.ItemAdapter
import com.example.transaction_project.home.Product
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment :Fragment(R.layout.home_fragment){

    private var isFabOpen = false
    private lateinit var fab_button: FloatingActionButton
    private lateinit var addList: FloatingActionButton
    private lateinit var filterList: FloatingActionButton


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //플로팅버튼 클릭 리스너
        fab_button = view.findViewById(R.id.fab_button)
        addList = view.findViewById(R.id.addList)
        filterList = view.findViewById(R.id.filter)

        // 플로팅액션 버튼이 위로 나오도록 애니메이션 설정
        fab_button.setOnClickListener {
            toggleFab()
        }




        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)

        //전반적인 이미지를 확인하기 위하여 직접 상품을 넣음, 차후에 DB를 이용
        val itemList = ArrayList<Product>()
        itemList.add(Product("아이폰 15 pro","","150만원","2023-10-23","reserve"))
        itemList.add(Product("캠핑 용품 팔아요","","50,000원","2023-9-25",""))
        itemList.add(Product("애플 매직 트랙패드","","105,000원","2023-8-25","complete"))
        itemList.add(Product("닌텐도 스위치","","250,000원","2023-7-25",""))
        itemList.add(Product("아이폰 14 pro","","130만원","2023-10-23","reserve"))
        itemList.add(Product("당근","","10,000원","2023-9-25",""))
        itemList.add(Product("플스","","405,000원","2023-8-25","complete"))
        itemList.add(Product("아아아","","2,500원","2023-7-25",""))

        val itemAdapter = ItemAdapter(itemList)
        itemAdapter.notifyDataSetChanged()

        recyclerView.adapter = itemAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)

    }

    //플로팅액션버튼 선택 시 글 작성, 필터 버튼이 나타나도록
    private fun toggleFab(){
        if(isFabOpen){
            ObjectAnimator.ofFloat(addList,"translationY",0f).apply { start() }
            ObjectAnimator.ofFloat(filterList,"translationY",0f).apply { start() }
            fab_button.setImageResource(R.drawable.plus)

        }
        else{
            ObjectAnimator.ofFloat(addList,"translationY",-200f).apply { start() }
            ObjectAnimator.ofFloat(filterList,"translationY",-400f).apply { start() }
            fab_button.setImageResource(R.drawable.close)
        }

        isFabOpen = (!isFabOpen)

    }

}