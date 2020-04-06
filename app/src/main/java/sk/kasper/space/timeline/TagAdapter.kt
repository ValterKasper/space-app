package sk.kasper.space.timeline

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sk.kasper.domain.model.Tag
import sk.kasper.space.databinding.LayoutTagBinding

class TagAdapter : RecyclerView.Adapter<TagAdapter.TagViewHolder>() {

    var items: List<Tag> = emptyList()
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
        fun update(tag: Tag) {
            binding.viewModel = TagViewModel(tag.type)
        }
    }
}