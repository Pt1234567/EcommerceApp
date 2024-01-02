package com.example.application.viewModel

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.application.data.User
import com.example.application.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private  val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
):ViewModel() {

    private  val _user= MutableStateFlow<Resource<User>>(Resource.unspeciFied())
    val user=_user.asStateFlow()


    fun getUser(){

        viewModelScope.launch {
            _user.emit(Resource.loading())
        }

        firestore.collection("user").document(firebaseAuth.uid!!)
            .addSnapshotListener{value,error->
                if(error!=null){
                    viewModelScope.launch {
                        _user.emit(Resource.failure(error.message.toString()))
                    }
                }
                else{
                    val user=value?.toObject(User::class.java)
                    user?.let {
                        viewModelScope.launch {
                            _user.emit(Resource.success(it))
                        }
                    }
                }
            }
    }
}