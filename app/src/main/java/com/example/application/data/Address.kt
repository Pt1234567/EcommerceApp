package com.example.application.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(
   val addressTitle:String,
    val name:String,
    val street:String,
    val phone:String,
    val city:String,
    val state:String
):Parcelable{
    constructor():this("","","","","","")
}
