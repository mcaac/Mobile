package com.example.status1v2

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FileItem(val FileName: String?, val Detalhes: String?):
    Parcelable