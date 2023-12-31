package com.example.transaction_project.fragment

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.transaction_project.R
import com.example.transaction_project.home.ItemAdapter
import com.example.transaction_project.home.Product
import com.example.transaction_project.home.ProductDetailActivity
import com.example.transaction_project.home.WriteActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
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
        initItemAdapter()
        getItemsList()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        InitFabButton(view)

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
            intent.putExtra("modify", false)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)

        }

        filterSelect()

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
                    val item = Product(
                        doc["productId"] as? String?: "",
                        doc["title"] as String,
                        doc["imgUrl"] as String ,
                        doc["price"] as String,
                        doc["crDate"] as Timestamp,
                        doc["status"] as String, doc["detail"] as? String?: "",
                        doc["category"] as? String?: "",
                        doc["sellerId"] as? String?: ""
                    )
                    itemList.add(item)
                }

                itemAdapter.setOnItemClickListener(object : ItemAdapter.OnItemClickListener {
                    override fun onItemClick(position: Int) {


                        val product = itemList[position]
                        // Product의 Id 만을 가져와 필요한 작업을 수행합니다.
                        val productId = product.productId


                        // 클릭 이벤트에 대한 작업 수행
                        // 예: ProductDetailActivity를 시작하거나 다른 작업 수행
                        val intent = Intent(context, ProductDetailActivity::class.java)
                        intent.putExtra("productId",productId)
                        context?.startActivity(intent)
                    }
                })

                itemAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                    exception ->
                Log.w("HomeFragment", "Error getting documents: $exception")
            }

    }


    //overloading method for filter
    private fun getItemsList(status : String){
        itemsCollectionRef
            .get()
            .addOnSuccessListener { result->
                itemList.clear()
                for(doc in result){
                    if(doc["status"] as String == status){
                        val item = Product(
                            doc["productId"] as? String?: "",
                            doc["title"] as String,
                            doc["imgUrl"] as String ,
                            doc["price"] as String,
                            doc["crDate"] as Timestamp,
                            doc["status"] as String,
                            doc["detail"] as? String?: "",
                            doc["category"] as? String?: "",
                            doc["sellerId"] as? String?: ""
                        )
                        itemList.add(item)
                    }
                }
                itemAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                    exception ->
                Log.w("HomeFragment", "Error getting documents: $exception")
            }

    }

    private fun filterSelect(){
        filterList.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(),it,Gravity.END)
            val inflater = popupMenu.menuInflater
            inflater.inflate(R.menu.popup_filter,popupMenu.menu)

            popupMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.all->{
                        getItemsList()
                        true
                    }
                    R.id.sell->{
                        getItemsList("판매중")
                        true
                    }
                    R.id.reserve->{
                        getItemsList("예약중")
                        true

                    }
                    R.id.complete->{
                        getItemsList("거래완료")
                        true
                    }
                    else->false
                }
            }
            popupMenu.show()
        }

    }
}