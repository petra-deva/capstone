package com.shiro.fishermanapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    val id: Int = 0,
    val name: String = "",
    val price: Long = 0L,
    val image: String = "",
    val description: String = ""
) : Parcelable