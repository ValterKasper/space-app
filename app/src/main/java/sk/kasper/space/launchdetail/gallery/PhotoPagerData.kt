package sk.kasper.space.launchdetail.gallery

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PhotoPagerData(
        val selectedPhotoIndex: Int,
        val photoItems: List<PhotoItem>): Parcelable