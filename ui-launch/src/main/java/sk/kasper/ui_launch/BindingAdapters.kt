package sk.kasper.ui_launch

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import sk.kasper.ui_common.tag.UiTag


@BindingAdapter("tags")
fun setTags(recyclerView: RecyclerView, tags: List<UiTag>) {
    val adapter = recyclerView.adapter
    if (adapter is TagAdapter) {
        adapter.items = tags
    }
}

