package sk.kasper.space.launchdetail.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import sk.kasper.space.BaseFragment
import sk.kasper.space.databinding.FragmentPhotoPagerBinding

class PhotoPagerFragment : BaseFragment() {

    private lateinit var viewPager: ViewPager
    private lateinit var binding: FragmentPhotoPagerBinding

    companion object {

        private const val KEY_PHOTO_PAGER_DATA = "key_photo_pager_data"

        fun newInstance(photoPagerData: PhotoPagerData): PhotoPagerFragment {
            return PhotoPagerFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_PHOTO_PAGER_DATA, photoPagerData)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentPhotoPagerBinding.inflate(inflater, container, false)
        binding.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        val galleryData = arguments!!.getParcelable<PhotoPagerData>(KEY_PHOTO_PAGER_DATA)!!
        viewPager = binding.viewPager
        viewPager.adapter = PhotoPagerAdapter(this, galleryData.photoItems)
        viewPager.currentItem = galleryData.selectedPhotoIndex

        return binding.root
    }

}
