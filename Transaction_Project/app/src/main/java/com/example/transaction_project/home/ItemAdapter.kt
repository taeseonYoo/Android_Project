package com.example.transaction_project.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.transaction_project.R



class ItemAdapter(val itemList:ArrayList<Product>):RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_list, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.tv_title.text = itemList[position].title
        holder.tv_price.text =itemList[position].price
        holder.tv_time.text = itemList[position].time

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

    }

    //판매상품의 상태를 확인하고 태그를 띄어준다.
    private fun setStatus(status: String, holder:ItemViewHolder ) {

        when(status){
            "reserve"->{ //예약중
                holder.tv_status.visibility = View.VISIBLE
                holder.tv_status.text = "예약중"
                holder.tv_status.setBackgroundResource(R.drawable.reserve_status)
            }
            "complete"->{ //거래완료
                holder.tv_status.visibility = View.VISIBLE
                holder.tv_status.text = "거래완료"
                holder.tv_status.setBackgroundResource(R.drawable.complete_status)
            }
            ""->{ //판매중
                holder.tv_status.visibility = View.INVISIBLE
            }

        }

    }


}