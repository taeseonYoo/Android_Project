package com.example.transaction_project.fragment

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.transaction_project.R
import com.example.transaction_project.home.ItemAdapter
import com.example.transaction_project.home.Product
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeFragment :Fragment(R.layout.home_fragment){

    private var isFabOpen = false
    private lateinit var fab_button: FloatingActionButton
    private lateinit var addList: FloatingActionButton
    private lateinit var filterList: FloatingActionButton

    private lateinit var itemAdapter : ItemAdapter
    private lateinit var recyclerView : RecyclerView

    private val db: FirebaseFirestore = Firebase.firestore
    val itemsCollectionRef = db.collection("Items")

    val itemList = arrayListOf<Product>()

   override fun onResume() {
        super.onResume()
        // 글 작성 액티비티 종료 후, recyclerView 업데이트를 위해 onResume() 오버라이드
        // 업데이트 메소드
        updateData()

    }


    private fun updateData() {


        // sharedPreferences 에 저장한 데이터 불러오기
        val sharedPreferences = requireContext().getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val title = sharedPreferences.getString("title", "")
        val price = sharedPreferences.getString("price", "")
        val category = sharedPreferences.getString("category", "")

        // 세부 내용
        val detail = sharedPreferences.getString("detail", "")


        if (title != "") {
            val previousItem =
                Product(title ?: "", "", price ?: "" + " 원", category ?: "", "reserve")
            itemList.add(previousItem)
        }

        // 중복 방지를 위해 데이터 삭제
        editor.clear()
        editor.apply()

        val recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerView)
        val itemAdapter = ItemAdapter(itemList)

        recyclerView?.adapter = itemAdapter
        recyclerView?.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        itemAdapter.notifyDataSetChanged()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)


        InitFabButton(view)
        initItemAdapter()
        getItemsList()

    }

    //FabButton 초기화
    private fun InitFabButton(view: View){
        fab_button = view.findViewById(R.id.fab_button)
        addList = view.findViewById(R.id.addList)
        filterList = view.findViewById(R.id.filter)

        // 플로팅액션 버튼이 위로 나오도록 애니메이션 설정
        fab_button.setOnClickListener {
            toggleFab()
        }

	// 글작성 버튼 클릭 -> 글쓰기 액티비티로 이동
        addList.setOnClickListener{
            val intent = Intent(activity, WriteActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)

        }
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



    private fun initItemAdapter(){

        itemAdapter = ItemAdapter(itemList)

        recyclerView.adapter = itemAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        itemAdapter.notifyDataSetChanged()
    }

    //아이템들을 DB에서 읽어옴 , 정상작동
    private fun getItemsList(){
        itemsCollectionRef
            .get()
            .addOnSuccessListener { result->
                itemList.clear()
                for(doc in result){
                    val item = Product(doc["title"] as String, doc["imgUrl"] as String ,doc["price"] as String, doc["time"] as Long, doc["status"] as String)
                    itemList.add(item)
                }
                itemAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                    exception ->
                Log.w("HomeFragment", "Error getting documents: $exception")
            }

    }

    // 새 글 작성 창에서 -> DB로 데이터 삽입 , 정상작동
    private fun pushItem(){
        val test = Product("연습","","",456789,"")
        itemsCollectionRef
            .add(test)
            .addOnSuccessListener {
                Snackbar.make(requireView(),"정상적으로 등록되었습니다.",Snackbar.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                exception ->
                Log.w("HomeFragment","Error adding documents: $exception")
            }
        itemAdapter.notifyDataSetChanged()
    }






}