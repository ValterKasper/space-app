package sk.kasper.space.launchdetail.gallery

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PhotoItem(val url: String,
                     val sourceName: String? = null,
                     val description: String? = null): Parcelable