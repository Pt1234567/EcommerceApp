package com.example.application.data

sealed class Category(val category:String){
    object books:Category("books")
    object clothing:Category("clothing")
    object mobile:Category("mobile")
    object electronics:Category("electronics")
    object homeandKitchen:Category("home&kitchen")
}
