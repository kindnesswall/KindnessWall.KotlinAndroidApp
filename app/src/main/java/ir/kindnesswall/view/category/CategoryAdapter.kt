package ir.kindnesswall.view.category


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ir.kindnesswall.R
import ir.kindnesswall.data.model.CategoryModel
import ir.kindnesswall.databinding.ItemCategoryListBinding
import ir.kindnesswall.utils.OnItemClickListener

class CategoryAdapter : RecyclerView.Adapter<CategoryViewHolder>() {

    private var multiSelection: Boolean = true
    lateinit var onClickCallback: OnItemClickListener

    private var items = ArrayList<CategoryModel>()

    fun setItems(items: List<CategoryModel>, multiSelection: Boolean = true) {
        this.items = items as ArrayList<CategoryModel>
        this.multiSelection = multiSelection
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int) = items[position].id.hashCode().toLong()

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_category_list,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.binding.item = items[position]

        holder.itemView.setOnClickListener {
            onClickCallback.onItemClicked(
                holder.adapterPosition,
                items[holder.adapterPosition]
            )
        }
    }
}

class CategoryViewHolder(val binding: ItemCategoryListBinding) :
    RecyclerView.ViewHolder(binding.root)