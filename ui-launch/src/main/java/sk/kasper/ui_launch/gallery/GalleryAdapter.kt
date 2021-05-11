package sk.kasper.space.launchdetail.gallery

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import sk.kasper.domain.model.Photo
import sk.kasper.ui_common.utils.RoundedRectangleTransformation
import sk.kasper.ui_common.utils.toPixels
import sk.kasper.ui_launch.R

class GalleryAdapter(val context: Context, val clickListener: PhotoClickListener)
    : RecyclerView.Adapter<GalleryAdapter.ImageViewHolder>() {

    interface PhotoClickListener {
        fun onPhotoClicked(photoPosition: Int)
    }

    private val items = mutableListOf<Photo>()
    private val cornerRadiusPx = R.dimen.rounded_corners_radius.toPixels(context).toFloat()
    private val imageViewHeightPx = R.dimen.launch_gallery_item_height.toPixels(context)

    fun setItems(items: List<Photo>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.gallery_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.update(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val image: ImageView = itemView.findViewById(R.id.galleryImageView)

        init {
            itemView.findViewById<View>(R.id.frameLayout).setOnClickListener(this)
        }

        fun update(photo: Photo) {
            Picasso.with(context)
                    .load(photo.thumbnailUrl)
                    .transform(RoundedRectangleTransformation(cornerRadiusPx))
                    .resize(0, imageViewHeightPx)
                    .into(image)
        }

        override fun onClick(view: View) {
            clickListener.onPhotoClicked(adapterPosition)
        }
    }

}