package sk.kasper.ui_launch.gallery

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import sk.kasper.space.launchdetail.gallery.PhotoFragment
import sk.kasper.space.launchdetail.gallery.PhotoItem

// Note: Initialize with the child fragment manager.
class PhotoPagerAdapter(fragment: Fragment, private val photoItems: List<PhotoItem>)
    : FragmentStatePagerAdapter(fragment.childFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int {
        return photoItems.size
    }

    override fun getItem(position: Int): Fragment {
        return PhotoFragment.newInstance(photoItems[position])
    }

}
