package com.example.application.data

data class Cart(
    val product: Product,
    var quantity:Int,
    val selectedSize:String?=null
) {
    constructor():this(Product(),1,null)
}