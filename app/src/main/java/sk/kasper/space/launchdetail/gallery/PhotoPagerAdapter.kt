package sk.kasper.space.launchdetail.gallery

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter

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
