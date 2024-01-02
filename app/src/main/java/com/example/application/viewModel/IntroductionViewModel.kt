package com.example.application.viewModel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.application.R
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroductionViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val sharedPreferences:SharedPreferences
):ViewModel() {

    private val _navigate=MutableStateFlow(0)
    val navigate:StateFlow<Int> =_navigate

    companion object{
        const val SHOPPING_ACTIVITY=23
        const val ACCOUNT_OPTION_FRAGMENT= R.id.action_fragmentIntroduction_to_fragmentAccountOption
    }
    init {
        val isButtonClicked=sharedPreferences.getBoolean("IntroductionKey",false)
        val user=firebaseAuth.currentUser

        if(user!=null){
           viewModelScope.launch {
               _navigate.emit(SHOPPING_ACTIVITY)
           }
        } else if(isButtonClicked){

            viewModelScope.launch {
                _navigate.emit(ACCOUNT_OPTION_FRAGMENT)
            }

        }else{

        }
    }

    fun startButtonClick(){
        sharedPreferences.edit().putBoolean("IntroductionKey",true).apply()
    }

}