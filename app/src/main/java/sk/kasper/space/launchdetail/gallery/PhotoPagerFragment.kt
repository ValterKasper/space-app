package sk.kasper.space.launchdetail.gallery

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import sk.kasper.space.R
import sk.kasper.space.databinding.FragmentPhotoPagerBinding
import sk.kasper.space.launchdetail.GalleryPositionModel
import sk.kasper.space.utils.provideViewModelActivityScoped

/**
 * A fragment for displaying a pager of images.
 */
class PhotoPagerFragment : Fragment() {

    private lateinit var viewPager: ViewPager
    private lateinit var galleryPositionModel: GalleryPositionModel
    private lateinit var binding: FragmentPhotoPagerBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        galleryPositionModel = provideViewModelActivityScoped()

        binding = FragmentPhotoPagerBinding.inflate(inflater, container, false)
        binding.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        val photoItems = arguments!!.getParcelableArrayList<PhotoItem>(KEY_PHOTO_ITEMS)!!
        viewPager = binding.viewPager
        viewPager.adapter = PhotoPagerAdapter(this, photoItems)
            // Set the current position and add a listener that will update the selection coordinator when
            // paging the images.
        viewPager.currentItem = galleryPositionModel.position
        viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
                override fun onPageSelected(position: Int) {
                    galleryPositionModel.position = position
                }
            })

        prepareSharedElementTransition()

        // Avoid a postponeEnterTransition on orientation change, and postpone only of first creation.
        if (savedInstanceState == null) {
            postponeEnterTransition()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        view?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    }

    /**
     * Prepares the shared element transition from and back to the grid fragment.
     */
    private fun prepareSharedElementTransition() {
        val transition = TransitionInflater.from(context)
                .inflateTransition(R.transition.image_shared_element_transition)
        sharedElementEnterTransition = transition

        // A similar mapping is set at the GridFragment with a setExitSharedElementCallback.
        setEnterSharedElementCallback(
                object : SharedElementCallback() {
                    override fun onMapSharedElements(names: List<String>?, sharedElements: MutableMap<String, View>?) {
                        // Locate the image view at the primary fragment (the PhotoFragment that is currently
                        // visible). To locate the fragment, call instantiateItem with the selection position.
                        // At this stage, the method will simply return the fragment at the position and will
                        // not create a new one.
                        val currentFragment = viewPager.adapter!!
                                .instantiateItem(viewPager, galleryPositionModel.position) as Fragment

                        val view = currentFragment.view ?: return

                        // Map the first shared element name to the child ImageView.
                        sharedElements!![names!![0]] = view.findViewById(R.id.image)
                    }
                })
    }

    companion object {

        private const val KEY_PHOTO_ITEMS = "key-photo-items"

        fun newInstance(photoItems: List<PhotoItem>): PhotoPagerFragment {
            val args = Bundle()
            args.putParcelableArrayList(KEY_PHOTO_ITEMS, ArrayList(photoItems))

            val fragment = PhotoPagerFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
