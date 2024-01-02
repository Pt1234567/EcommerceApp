package com.example.application.viewModel

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.application.EcommerceApplication
import com.example.application.data.User
import com.example.application.util.RegisterValidation
import com.example.application.util.Resource
import com.example.application.util.validateEmail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage:StorageReference,
    app:Application
) :AndroidViewModel(app){

    private val _user= MutableStateFlow<Resource<User>>(Resource.unspeciFied())
    val user=_user.asStateFlow()

    private val _updateInfo=MutableStateFlow<Resource<User>>(Resource.unspeciFied())
    val updateInfo=_updateInfo.asStateFlow()

    private val _resetPassword= MutableSharedFlow<Resource<String>>()
    val resetpassword=_resetPassword.asSharedFlow()



    init {
        getUser()
    }
    fun getUser(){
        viewModelScope.launch {
            _user.emit(Resource.loading())
        }
        firestore.collection("user").document(firebaseAuth.uid!!).get()
            .addOnSuccessListener {
                val user=it.toObject(User::class.java)
                user?.let {
                    viewModelScope.launch{
                        _user.emit(Resource.success(it))
                    }
                }
            }.addOnFailureListener {
                    viewModelScope.launch{
                        _user.emit(Resource.failure(it.message.toString()))
                    }
            }
    }


    fun updateUserInfo(user:User,imageUri:Uri?){


        val validateInputs= validateEmail(user.email) is RegisterValidation.Success
                && user.firstName.trim().isNotEmpty()
                && user.lastName.trim().isNotEmpty()

        if(!validateInputs){
            viewModelScope.launch {
                _updateInfo.emit(Resource.failure("Check your inputs"))
            }
            return
        }

        viewModelScope.launch {
            _updateInfo.emit(Resource.loading())
        }

        if(imageUri==null){
            saveUserInfo(user,true)
        }else{
            saveUserWithNewImage(user,imageUri)
        }

    }

    private fun saveUserWithNewImage(user: User, imageUri: Uri) {
       viewModelScope.launch {
           try {
               val imgBitmap=MediaStore.Images.Media.getBitmap(getApplication<EcommerceApplication>().contentResolver,imageUri)
               val byteArrayOutputStream=ByteArrayOutputStream()
               imgBitmap.compress(Bitmap.CompressFormat.JPEG,96,byteArrayOutputStream)
               val imgByteArray=byteArrayOutputStream.toByteArray()
               val imageDirectory=storage.child("profileImages/${firebaseAuth.uid}/${UUID.randomUUID()}")
               val result=imageDirectory.putBytes(imgByteArray).await()
               val imageUrl=result.storage.downloadUrl.await().toString()
                saveUserInfo(user.copy(imagePath = imageUrl),true)
           }

           catch (e:Exception){
             viewModelScope.launch {
                 _updateInfo.emit(Resource.failure(e.message.toString()))
             }
           }
       }
    }

    private fun saveUserInfo(user: User, shouldRetrievedoldImage: Boolean) {
          firestore.runTransaction {transaction->

              val documentRef=firestore.collection("user").document(firebaseAuth.uid!!)
              val currUser=transaction.get(documentRef).toObject(User::class.java)
              if(shouldRetrievedoldImage){

                  val newUser=user.copy(imagePath = currUser?.imagePath)
                  transaction.set(documentRef,newUser)
              }else{
                  transaction.set(documentRef,user)
              }

          }.addOnSuccessListener {
              viewModelScope.launch {
                  _updateInfo.emit(Resource.success(user))
              }
          }.addOnFailureListener {
              viewModelScope.launch{
                  _updateInfo.emit(Resource.failure(it.message.toString()))
              }
          }
    }

    fun resetPassWord(email:String){
        viewModelScope.launch {
            _resetPassword.emit(Resource.loading())
        }

        firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener {
            viewModelScope.launch {
                _resetPassword.emit(Resource.success(email))
            }
        }
            .addOnFailureListener {
                viewModelScope.launch{
                    _resetPassword.emit(Resource.failure(it.message.toString()))
                }
            }
    }
}