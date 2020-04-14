package sk.kasper.space.launchdetail.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import sk.kasper.space.databinding.FragmentPhotoBinding
import sk.kasper.space.utils.provideViewModel

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
        binding.viewModel = viewModel

        Picasso.with(context)
                .load(viewModel.url)
                .into(binding.image)

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
