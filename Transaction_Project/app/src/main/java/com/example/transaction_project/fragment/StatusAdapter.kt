package com.example.transaction_project.fragment
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.transaction_project.R

class StatusAdapter(
    private val statusList: List<Status>,
    private val statusBtn: Button,
    private val recyclerView: RecyclerView
) : RecyclerView.Adapter<StatusAdapter.StatusViewHolder>() {

    interface StatusClickListener {
        fun onStatusClicked(statusName: String)
    }

    private var selectedStatusPosition: Int = RecyclerView.NO_POSITION
    private var statusClickListener : StatusClickListener? = null

    fun setStatusClickListener(listener: StatusClickListener) {
        this.statusClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.status_item, parent, false)
        return StatusViewHolder(view)
    }

    override fun onBindViewHolder(holder: StatusViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val status = statusList[position]
        holder.statusName.text = status.status

        holder.itemView.setOnClickListener {
            selectedStatusPosition = position
            notifyDataSetChanged()
            recyclerView.visibility = View.GONE
            statusClickListener?.onStatusClicked(status.status)
        }
    }

    override fun getItemCount(): Int {
        return statusList.size
    }

    inner class StatusViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val statusName: TextView = itemView.findViewById(R.id.statusName)
    }
}