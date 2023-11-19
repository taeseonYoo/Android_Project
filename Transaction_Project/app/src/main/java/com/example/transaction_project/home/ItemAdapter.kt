package com.example.transaction_project.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.transaction_project.R
import java.text.SimpleDateFormat
import java.util.Date



class ItemAdapter(val itemList:ArrayList<Product>): RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    // 클릭 리스너 변수를 선언합니다.
    private var listener: OnItemClickListener? = null

    // 클릭 리스너를 설정하는 메서드를 추가합니다.
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_list, parent, false)
        return ItemViewHolder(view)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        //Long타입의 데이터를 포맷하는 과정. 글 작성 시간을 데이터로 남기기
        val format = SimpleDateFormat("MM월 dd일")
        val date = Date(itemList[position].time)

        holder.tv_title.text = itemList[position].title
        holder.tv_price.text ="${itemList[position].price} 원"
        holder.tv_time.text = format.format(date).toString()

        //imgUrl로 이미지 불러오기
        if(itemList[position].imgUrl.isNotEmpty()){
            Glide.with(holder.tv_img)
                .load(itemList[position].imgUrl)
                .skipMemoryCache(true)
                .error(R.drawable.user)
                .into(holder.tv_img)
        }

        //상태를 확인하고 처리함
        setStatus(itemList[position].status, holder)


    }

    override fun getItemCount(): Int {
        return itemList.count()
    }


    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_time = itemView.findViewById<TextView>(R.id.date)
        val tv_title =  itemView.findViewById<TextView>(R.id.title)
        val tv_price = itemView.findViewById<TextView>(R.id.price)
        val tv_status = itemView.findViewById<TextView>(R.id.status)
        val tv_img = itemView.findViewById<ImageView>(R.id.imageView)

        init {
            // 클릭 이벤트를 처리하는 부분입니다.
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(position)
                }
            }
        }
    }
    //판매상품의 상태를 확인하고 태그를 띄어준다.
    private fun setStatus(status: String, holder:ItemViewHolder ) {

        when(status){
            "예약중"->{ //예약중
                holder.tv_status.visibility = View.VISIBLE
                holder.tv_status.text = "예약중"
                holder.tv_status.setBackgroundResource(R.drawable.reserve_status)
            }
            "거래완료"->{ //거래완료
                holder.tv_status.visibility = View.VISIBLE
                holder.tv_status.text = "거래완료"
                holder.tv_status.setBackgroundResource(R.drawable.complete_status)
            }
            "판매중"->{ //판매중
                holder.tv_status.visibility = View.VISIBLE
                holder.tv_status.text="판매중"
                holder.tv_status.setBackgroundResource(R.drawable.onsale_status)
            }

        }

    }


}