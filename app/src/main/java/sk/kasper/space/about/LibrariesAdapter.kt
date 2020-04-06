package sk.kasper.space.about

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import sk.kasper.space.R


class LibrariesAdapter : RecyclerView.Adapter<LibrariesAdapter.ViewHolder>() {

    private val libraries = listOf(
            Library("Android support libraries", "https://developer.android.com/topic/libraries/support-library"),
            Library("Prettytime", "https://github.com/ocpsoft/prettytime"),
            Library("Dagger 2", "https://google.github.io/dagger/"),
            Library("Timber", "https://github.com/JakeWharton/timber"),
            Library("Threetenabp", "https://github.com/JakeWharton/ThreeTenABP"),
            Library("Picasso", "http://square.github.io/picasso/"),
            Library("PhotoView", "https://github.com/chrisbanes/PhotoView"),
            Library("OkHttp", "http://square.github.io/okhttp/"),
            Library("Retrofit 2", "http://square.github.io/retrofit/")
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.library_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return libraries.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val library = libraries[position]
        holder.itemView.findViewById<TextView>(R.id.titleView).text = library.name
        holder.itemView.findViewById<TextView>(R.id.linkView).text = library.link
        holder.itemView.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(library.link)
            holder.itemView.context.startActivity(i)
        }
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view)

    data class Library(val name: String, val link: String)

}
