package com.example.transaction_project.fragment

import android.animation.ObjectAnimator
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
                    val item = Product(doc["title"] as String, doc["imgUrl"] as String ,doc["price"] as String, doc["time"] as Long, doc["status"] as String, doc["content"] as String, doc["writer"] as String, doc["category"] as String)
                    itemList.add(item)
                }

                itemAdapter.setOnItemClickListener(object : ItemAdapter.OnItemClickListener {
                    override fun onItemClick(position: Int) {
                        // 클릭한 항목의 정보를 가져옵니다.
                        val product = itemList[position]

                        // Product의 정보를 사용하여 필요한 작업을 수행합니다.
                        val title = product.title
                        val imgUrl = product.imgUrl
                        val writer = product.writer
                        val content = product.content
                        val price = product.price
                        val time = product.time
                        val status = product.status
                        val category = product.category

                        // 클릭 이벤트에 대한 작업 수행
                        // 예: ProductDetailActivity를 시작하거나 다른 작업 수행
                        val intent = Intent(context, ProductDetailActivity::class.java)

                        intent.putExtra("title", title)
                        intent.putExtra("imgUrl", imgUrl)
                        intent.putExtra("price", price)
                        intent.putExtra("time", time)
                        intent.putExtra("writer", writer)
                        intent.putExtra("content", content)
                        intent.putExtra("status", status)
                        intent.putExtra("category", category)
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

    // 새 글 작성 창에서 -> DB로 데이터 삽입 , 정상작동
    /* private fun pushItem(){
         val test = Product("연습","","",456789,"")
         itemsCollectionRef
             .add(test)
             .addOnSuccessListener {
                 Snackbar.make(requireView(),"정상적으로 등록되었습니다.", Snackbar.LENGTH_SHORT).show()
             }
             .addOnFailureListener {
                     exception ->
                 Log.w("HomeFragment","Error adding documents: $exception")
             }
         itemAdapter.notifyDataSetChanged()
     }*/






}