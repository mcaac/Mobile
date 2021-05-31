package com.example.photoalbum

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
@Parcelize
data class DataModel(var title: String, var image: Uri):Parcelable