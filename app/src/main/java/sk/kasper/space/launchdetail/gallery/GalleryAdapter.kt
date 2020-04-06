package sk.kasper.space.launchdetail.gallery

import android.transition.TransitionSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import sk.kasper.domain.model.Photo
import sk.kasper.space.R
import sk.kasper.space.analytics.Analytics
import sk.kasper.space.launchdetail.GalleryPositionModel
import sk.kasper.space.utils.RoundedRectangleTransformation
import sk.kasper.space.utils.toPixels
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Constructs a new grid adapter for the given [Fragment].
 */
class GalleryAdapter(fragment: Fragment, val galleryPositionModel: GalleryPositionModel)
    : RecyclerView.Adapter<GalleryAdapter.ImageViewHolder>() {

    private val items = mutableListOf<Photo>()
    private val viewHolderListener: ViewHolderListener
    private val cornerRadiusPx = R.dimen.rounded_corners_radius.toPixels(fragment.requireContext()).toFloat()
    private val imageViewHeightPx = R.dimen.launch_gallery_item_height.toPixels(fragment.requireContext())

    /**
     * A listener that is attached to all ViewHolders to handle image loading events and clicks.
     */
    interface ViewHolderListener {

        fun onLoadCompleted(view: ImageView, adapterPosition: Int)
        fun onItemClicked(view: View, adapterPosition: Int)

    }

    init {
        this.viewHolderListener = ViewHolderListenerImpl(fragment)
    }

    fun setItems(items: List<Photo>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.gallery_list_item, parent, false)
        return ImageViewHolder(view, viewHolderListener)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.update(items[position])
    }

    override fun getItemCount(): Int = items.size

    // todo zvaz ci ten prechod nezmazes

    /**
     * Default [ViewHolderListener] implementation.
     */
    private inner class ViewHolderListenerImpl internal constructor(private val fragment: Fragment) : ViewHolderListener {
        private val enterTransitionStarted: AtomicBoolean = AtomicBoolean()

        override fun onLoadCompleted(view: ImageView, adapterPosition: Int) {
            // Call startPostponedEnterTransition only when the 'selected' image loading is completed.
            if (galleryPositionModel.position != adapterPosition) {
                return
            }
            if (enterTransitionStarted.getAndSet(true)) {
                return
            }
            fragment.startPostponedEnterTransition()
        }

        /**
         * Handles a view click by setting the current position to the given `position` and
         * starting a [PhotoPagerFragment] which displays the image at the position.
         *
         * @param view the clicked [ImageView] (the shared element view will be re-mapped at the
         * GridFragment's SharedElementCallback)
         * @param adapterPosition the selected view position
         */
        override fun onItemClicked(view: View, adapterPosition: Int) {
            // Update the position.
            galleryPositionModel.position = adapterPosition

            // Exclude the clicked card from the exit transition (e.g. the card will disappear immediately
            // instead of fading out with the rest to prevent an overlapping animation of fade and move).
            (fragment.exitTransition as TransitionSet).excludeTarget(view, true)

            val transitioningView = view.findViewById<ImageView>(R.id.galleryImageView)
            val photoItems = items.map { PhotoItem(it.fullSizeUrl, it.sourceName, it.description) }
            fragment.fragmentManager!!
                    .beginTransaction()
                    .setReorderingAllowed(true) // Optimize for shared element transition
                    .addSharedElement(transitioningView, transitioningView.transitionName)
                    .replace(R.id.fragment_container, PhotoPagerFragment.newInstance(photoItems), PhotoPagerFragment::class.java
                            .simpleName)
                    .addToBackStack(null)
                    .commit()

            Analytics.log(Analytics.Event.SELECT_LAUNCH_GALLERY_PHOTO, hashMapOf(
                    Analytics.Param.ITEM_NAME to photoItems[adapterPosition].url
            ))
        }
    }

    /**
     * ViewHolder for the gallery images.
     */
    inner class ImageViewHolder(
            itemView: View,
            private val viewHolderListener: ViewHolderListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val image: ImageView = itemView.findViewById(R.id.galleryImageView)
        private val context = image.context

        init {
            itemView.findViewById<View>(R.id.frameLayout).setOnClickListener(this)
        }

        /**
         * Binds this view holder to the given adapter position.
         *
         * The binding will load the image into the image view, as well as set its transition name for
         * later.
         */
        fun update(photo: Photo) {
            Picasso.with(context)
                    .load(photo.thumbnailUrl)
                    .transform(RoundedRectangleTransformation(cornerRadiusPx))
                    .resize(0, imageViewHeightPx)
                    .into(image, object : Callback {

                        override fun onSuccess() {
                            viewHolderListener.onLoadCompleted(image, adapterPosition)
                        }

                        override fun onError() {
                            viewHolderListener.onLoadCompleted(image, adapterPosition)
                        }

                    })

            // Set the string value of the image resource as the unique transition name for the view.
            image.transitionName = photo.toString()
        }

        override fun onClick(view: View) {
            viewHolderListener.onItemClicked(view, adapterPosition)
        }
    }

}