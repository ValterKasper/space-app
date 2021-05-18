package sk.kasper.ui_launch.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import sk.kasper.space.launchdetail.gallery.PhotoItem
import sk.kasper.ui_common.BaseFragment
import sk.kasper.ui_common.utils.viewModels
import sk.kasper.ui_launch.databinding.FragmentPhotoBinding

class PhotoFragment : BaseFragment() {

    private val viewModel: PhotoFragmentViewModel by viewModels {
        val imageRes = requireArguments().getParcelable<PhotoItem>(KEY_PHOTO_ITEM)!!
        PhotoFragmentViewModel(imageRes)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPhotoBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        Picasso.with(context)
            .load(viewModel.url)
            .into(binding.image)

        return binding.root
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
