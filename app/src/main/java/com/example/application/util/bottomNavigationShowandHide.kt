package com.example.application.util

import android.view.View
import androidx.fragment.app.Fragment
import com.example.application.R
import com.example.application.activities.ShoppingActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

fun Fragment.hideBottomNavigation(){
    val bottomNavigationView=(activity as ShoppingActivity).findViewById<BottomNavigationView>(R.id.bottomNavigation)
    bottomNavigationView.visibility=View.GONE
}

fun Fragment.ShowBottomNavigation(){
    val bottomNavigationView=(activity as ShoppingActivity).findViewById<BottomNavigationView>(R.id.bottomNavigation)
    bottomNavigationView.visibility=View.VISIBLE
}