package rs.edu.viser.pmu.chatappdiplomski.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class KorisnikModel(val uid: String, val username: String, val slikaUrl: String): Parcelable {
    constructor(): this("","","")
}