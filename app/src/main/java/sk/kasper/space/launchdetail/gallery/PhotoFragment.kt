package sk.kasper.space.launchdetail.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import sk.kasper.space.databinding.FragmentPhotoBinding
import sk.kasper.space.utils.provideViewModel

/**
 * A fragment for displaying a photo.
 */
class PhotoFragment : Fragment() {

    private lateinit var viewModel: PhotoFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = provideViewModel {
            val imageRes = requireNotNull(arguments!!.getParcelable<PhotoItem>(KEY_PHOTO_ITEM))
            PhotoFragmentViewModel(imageRes)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentPhotoBinding.inflate(inflater, container, false)

        // Just like we do when binding views at the gallery, we set the transition name to be the string
        // value of the image res.
        binding.viewModel = viewModel
        binding.image.transitionName = viewModel.url

        Picasso.with(context)
                .load(viewModel.url)
                .into(binding.image, object : Callback {

                    override fun onSuccess() {
                        // The postponeEnterTransition is called on the parent PhotoPagerFragment, so the
                        // startPostponedEnterTransition() should also be called on it to get the transition
                        // going in case of a failure.
                        parentFragment!!.startPostponedEnterTransition()
                    }

                    override fun onError() {
                        // The postponeEnterTransition is called on the parent PhotoPagerFragment, so the
                        // startPostponedEnterTransition() should also be called on it to get the transition
                        // going when the image is ready.
                        parentFragment!!.startPostponedEnterTransition()
                    }

                })

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        view?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    }

    companion object {

        private const val KEY_PHOTO_ITEM = "key-photo-item"

        fun newInstance(photoItem: PhotoItem): PhotoFragment {
            val argument = Bundle()
            argument.putParcelable(KEY_PHOTO_ITEM, photoItem)
            val fragment = PhotoFragment()
            fragment.arguments = argument
            return fragment
        }

    }
}
