package com.example.application.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cart(
    val product: Product,
    var quantity:Int,
    val selectedSize:String?=null
) :Parcelable{
    constructor():this(Product(),1,null)
}