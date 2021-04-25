package sk.kasper.ui_timeline.utils

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import sk.kasper.domain.model.Tag
import sk.kasper.ui_timeline.TagAdapter


// todo copied from app module
@BindingAdapter("tags")
fun setTags(recyclerView: RecyclerView, tags: List<Tag>) {
    val adapter = recyclerView.adapter
    if (adapter is TagAdapter) {
        adapter.items = tags
    }
}

