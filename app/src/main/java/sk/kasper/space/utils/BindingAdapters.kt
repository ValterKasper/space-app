package sk.kasper.space.utils

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import sk.kasper.domain.model.Tag
import sk.kasper.space.timeline.TagAdapter


@BindingAdapter("tags")
fun setTags(recyclerView: RecyclerView, tags: List<Tag>) {
    val adapter = recyclerView.adapter
    if (adapter is TagAdapter) {
        adapter.items = tags
    }
}

