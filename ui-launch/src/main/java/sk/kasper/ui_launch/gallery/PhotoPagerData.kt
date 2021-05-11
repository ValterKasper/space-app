package sk.kasper.ui_launch.gallery

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import sk.kasper.space.launchdetail.gallery.PhotoItem

@Parcelize
data class PhotoPagerData(
        val selectedPhotoIndex: Int,
        val photoItems: List<PhotoItem>): Parcelable