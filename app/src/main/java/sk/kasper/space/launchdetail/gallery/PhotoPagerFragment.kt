package sk.kasper.space.launchdetail.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.viewpager.widget.ViewPager
import sk.kasper.space.BaseFragment
import sk.kasper.space.R
import sk.kasper.space.databinding.FragmentPhotoPagerBinding

class PhotoPagerFragment : BaseFragment() {

    private lateinit var viewPager: ViewPager
    private lateinit var binding: FragmentPhotoPagerBinding

    private val args: PhotoPagerFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentPhotoPagerBinding.inflate(inflater, container, false)
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        binding.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        viewPager = binding.viewPager
        viewPager.adapter = PhotoPagerAdapter(this, args.photoPagerData.photoItems)
        viewPager.currentItem = args.photoPagerData.selectedPhotoIndex

        return binding.root
    }

}
