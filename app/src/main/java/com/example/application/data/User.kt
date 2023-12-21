package com.example.application.data

data class User(
    val firstName:String,
    val lastName:String,
    val email: String,
    val imagePath:String?=null
)
//whenever we are dealing with fire base we need an empty constructor
{
    constructor() : this("", "", "", "")
}
