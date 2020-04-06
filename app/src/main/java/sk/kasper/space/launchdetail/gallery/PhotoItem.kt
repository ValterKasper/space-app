package sk.kasper.space.launchdetail.gallery

import android.os.Parcel
import android.os.Parcelable

// todo pouzi parcelize
data class PhotoItem(val url: String,
                     val sourceName: String? = null,
                     val description: String? = null): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(url)
        parcel.writeString(sourceName)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PhotoItem> {
        override fun createFromParcel(parcel: Parcel): PhotoItem {
            return PhotoItem(parcel)
        }

        override fun newArray(size: Int): Array<PhotoItem?> {
            return arrayOfNulls(size)
        }
    }

}