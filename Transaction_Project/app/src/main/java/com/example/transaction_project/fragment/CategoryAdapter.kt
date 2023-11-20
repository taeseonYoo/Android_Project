package com.example.transaction_project.fragment
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.transaction_project.R

class CategoryAdapter(
    private val categories: List<Category>,
    private val categoryBtn: Button,
    private val recyclerView: RecyclerView
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    interface CategoryClickListener {
        fun onCategoryClicked(categoryName: String)
    }

    private var selectedCategoryPosition: Int = RecyclerView.NO_POSITION
    private var categoryClickListener : CategoryClickListener? = null

    fun setCategoryClickListener(listener: CategoryClickListener) {
        this.categoryClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val category = categories[position]
        holder.categoryName.text = category.name

        holder.itemView.setOnClickListener {
            selectedCategoryPosition = position
            notifyDataSetChanged()
            recyclerView.visibility = View.GONE
            categoryClickListener?.onCategoryClicked(category.name)
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryName: TextView = itemView.findViewById(R.id.categoryName)
    }
}
