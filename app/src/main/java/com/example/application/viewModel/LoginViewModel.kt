package com.example.application.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.application.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseAuth:FirebaseAuth
):ViewModel() {

    private val _login=MutableSharedFlow<Resource<FirebaseUser>>()
    val login=_login.asSharedFlow()


    private val _resetPassword=MutableSharedFlow<Resource<String>>()
    val resetpassword=_resetPassword.asSharedFlow()

    fun loginWithEmailAndPass(email:String,password:String){
        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                viewModelScope.launch {
                    it.user?.let {
                        _login.emit(Resource.success(it))
                    }
                }
            }
            .addOnFailureListener {
                  viewModelScope.launch {
                      _login.emit(Resource.failure(it.message.toString()))
                  }
            }
    }

    fun resetPassword(email:String){
        viewModelScope.launch {
            _resetPassword.emit(Resource.loading())
        }
            firebaseAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener {
                 viewModelScope.launch {
                     _resetPassword.emit(Resource.success(email))
                 }
                }.addOnFailureListener {
                    viewModelScope.launch {
                        _resetPassword.emit(Resource.failure(it.message.toString()))
                    }
                }

    }

}