package sk.kasper.ui_timeline.filter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import sk.kasper.ui_timeline.R
import sk.kasper.ui_timeline.TagViewModel
import sk.kasper.ui_timeline.databinding.FilterRocketListItemBinding
import sk.kasper.ui_timeline.databinding.FilterTagListItemBinding

class TimelineFilterItemsAdapter(context: Context, val filterSelectionListener: FilterSelectionListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    private val filterItems: MutableList<FilterItem> = mutableListOf()

    fun setFilterItems(items: List<FilterItem>) {
        filterItems.clear()
        filterItems.addAll(items)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int) = filterItems[position].getType()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            when (viewType) {
                FilterItem.HEADER_TYPE -> {
                    HeaderViewHolder(inflater.inflate(R.layout.filter_header_list_item, parent, false))
                }
                FilterItem.TAG_TYPE -> {
                    TagViewHolder(inflater.inflate(R.layout.filter_tag_list_item, parent, false))
                }
                FilterItem.ROCKET_TYPE -> {
                    RocketViewHolder(inflater.inflate(R.layout.filter_rocket_list_item, parent, false))
                }
                else -> throw IllegalArgumentException("Unknown view type: $viewType")
            }

    override fun getItemCount() = filterItems.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when (holder) {
        is HeaderViewHolder -> {
            holder.update((filterItems[position] as FilterItem.HeaderFilterItem))
        }
        is TagViewHolder -> {
            holder.update((filterItems[position] as FilterItem.TagFilterItem))
        }
        is RocketViewHolder -> {
            holder.update((filterItems[position] as FilterItem.RocketFilterItem))
        }
        else -> {}
    }

    class HeaderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView as TextView

        fun update(headerFilterItem: FilterItem.HeaderFilterItem) {
            textView.setText(headerFilterItem.stringRes)
        }
    }

    inner class TagViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), CompoundButton.OnCheckedChangeListener {

        val binding: FilterTagListItemBinding = FilterTagListItemBinding.bind(itemView)

        init {
            binding.root.setOnClickListener {
                binding.filterCheckbox.toggle()
            }
        }

        fun update(tagFilterItem: FilterItem.TagFilterItem) {
            binding.viewModel = TagViewModel(tagFilterItem.tagType)
            setChecked(tagFilterItem.selected)
        }

        private fun setChecked(checked: Boolean) {
            // avoid dispatching changes when recycling views
            binding.filterCheckbox.setOnCheckedChangeListener(null)
            binding.filterCheckbox.isChecked = checked
            binding.filterCheckbox.setOnCheckedChangeListener(this)
        }

        override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
            val tagFilterItem = filterItems[adapterPosition] as FilterItem.TagFilterItem
            val item = tagFilterItem.copy(selected = isChecked)
            filterItems[adapterPosition] = item
            filterSelectionListener.onTagFilterItemChanged(item)
        }
    }

    inner class RocketViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), CompoundButton.OnCheckedChangeListener {

        val binding: FilterRocketListItemBinding = FilterRocketListItemBinding.bind(itemView)

        init {
            binding.root.setOnClickListener {
                binding.filterCheckbox.toggle()
            }
        }

        fun update(rocketFilterItem: FilterItem.RocketFilterItem) {
            binding.viewModel = RocketViewModel(rocketFilterItem.rocketId)
            setChecked(rocketFilterItem.selected)
        }

        private fun setChecked(checked: Boolean) {
            // avoid dispatching changes when recycling views
            binding.filterCheckbox.setOnCheckedChangeListener(null)
            binding.filterCheckbox.isChecked = checked
            binding.filterCheckbox.setOnCheckedChangeListener(this)
        }

        override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
            val rocketFilterItem = filterItems[adapterPosition] as FilterItem.RocketFilterItem
            val item = rocketFilterItem.copy(selected = isChecked)
            filterItems[adapterPosition] = item
            filterSelectionListener.onRocketFilterItemChanged(item)
        }
    }

}