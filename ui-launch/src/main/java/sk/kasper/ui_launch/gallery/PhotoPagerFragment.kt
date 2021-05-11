package sk.kasper.ui_launch.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import sk.kasper.ui_common.BaseFragment
import sk.kasper.ui_launch.R
import sk.kasper.ui_launch.databinding.FragmentPhotoPagerBinding

class PhotoPagerFragment : BaseFragment() {

    private lateinit var viewPager: ViewPager
    private lateinit var binding: FragmentPhotoPagerBinding

    // todo
//    private val args: PhotoPagerFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPhotoPagerBinding.inflate(inflater, container, false)
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        binding.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        viewPager = binding.viewPager
        // todo
//        viewPager.adapter = PhotoPagerAdapter(this, args.photoPagerData.photoItems)
//        viewPager.currentItem = args.photoPagerData.selectedPhotoIndex

        return binding.root
    }

}
