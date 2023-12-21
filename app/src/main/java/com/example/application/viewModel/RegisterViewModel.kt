package com.example.application.viewModel

import androidx.lifecycle.ViewModel
import com.example.application.data.User
import com.example.application.util.Constants.USER_COLLECTION
import com.example.application.util.RegisterFieldState
import com.example.application.util.RegisterValidation
import com.example.application.util.Resource
import com.example.application.util.validateEmail
import com.example.application.util.validatePassword
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val db:FirebaseFirestore
): ViewModel() {

    //MutableStateFlow is a type of state flow that allows you to emit updates to its value.
    private val _register= MutableStateFlow<Resource<User>>(Resource.unspeciFied())
    // external components can observe changes in the registration state through this flow.
    val register: Flow<Resource<User>> = _register

    private val _validate= Channel<RegisterFieldState> {}
    val validate=_validate.receiveAsFlow()

    fun createAccountWithEmailAndPassword(user: User, password:String){

       if(checkValidation(user, password)) {

           runBlocking {
               //_register.emit(Resource.loading())
           }
           firebaseAuth.createUserWithEmailAndPassword(user.email, password)
               .addOnSuccessListener {
                   it.user?.let {
                       saveUserData(it.uid,user)

                   }
               }.addOnFailureListener {
                   _register.value = Resource.failure(it.message.toString())
               }
       }
        else{
            val registerValidate=RegisterFieldState(
                validateEmail(user.email),
                validatePassword(password)
            )
           runBlocking {
               _validate.send(registerValidate)
           }
       }
    }

    private fun saveUserData(userUid:String ,user: User) {
              db.collection(USER_COLLECTION)
                  .document(userUid)
                  .set(user)
                  .addOnSuccessListener {
                      _register.value = Resource.success(user);
                  }
                  .addOnFailureListener {
                      _register.value=Resource.failure(it.message.toString())
                  }
    }

    private fun checkValidation(user: User, password: String):Boolean {
        val emailValidation = validateEmail(user.email)
        val passwordValidation = validatePassword(password)
        val shouldRegister =
            emailValidation is RegisterValidation.Success && passwordValidation is RegisterValidation.Success

        return shouldRegister
    }

}