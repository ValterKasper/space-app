package sk.kasper.ui_launch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sk.kasper.ui_common.tag.UiTag
import sk.kasper.ui_launch.databinding.LayoutTagBinding

class TagAdapter : RecyclerView.Adapter<TagAdapter.TagViewHolder>() {

    var items: List<UiTag> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        return TagViewHolder(LayoutTagBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        holder.update(items[position])
    }

    class TagViewHolder(private val binding: LayoutTagBinding): RecyclerView.ViewHolder(binding.root) {
        fun update(tag: UiTag) {
            binding.tag = tag
        }
    }
}