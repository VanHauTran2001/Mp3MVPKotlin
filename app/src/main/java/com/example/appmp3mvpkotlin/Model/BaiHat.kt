package Model

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator

class BaiHat : Parcelable {
    var nameSong: String? = null
    var nameSinger: String? = null
    var linkSong: String? = null

    constructor(nameSong: String?, nameSinger: String?, linkSong: String?) {
        this.nameSong = nameSong
        this.nameSinger = nameSinger
        this.linkSong = linkSong
    }

    constructor()

    protected constructor(it: Parcel) {
        nameSong = it.readString()
        nameSinger = it.readString()
        linkSong = it.readString()
    }

    @JvmName("getNameSong1")
    fun getNameSong(): String? {
        return nameSong
    }

    @JvmName("getNameSinger1")
    fun getNameSinger(): String? {
        return nameSinger
    }

    @JvmName("getLinkSong1")
    fun getLinkSong(): String? {
        return linkSong
    }

    @JvmName("setLinkSong1")
    fun setLinkSong(linkSong: String?) {
        this.linkSong = linkSong
    }
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeString(nameSong)
        parcel.writeString(nameSinger)
        parcel.writeString(linkSong)
    }

    companion object {
        @JvmField
        val CREATOR: Creator<BaiHat?> = object : Creator<BaiHat?> {
            override fun createFromParcel(parcel: Parcel): BaiHat? {
                return BaiHat(parcel)
            }

            override fun newArray(size: Int): Array<BaiHat?> {
                return arrayOfNulls(size)
            }
        }
    }
}