package com.example.application.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.application.data.Address
import com.example.application.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BillingViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
):ViewModel() {


    private  val _address=MutableStateFlow<Resource<List<Address>>>(Resource.unspeciFied())
    val address=_address.asStateFlow()

    init {
        getUserAddress()
    }
    fun getUserAddress(){
        viewModelScope.launch {
            _address.emit(Resource.loading())
        }

        firestore.collection("user").document(firebaseAuth.uid!!).collection("address")
            .addSnapshotListener{value,error->
                if(error!=null){
                    viewModelScope.launch { _address.emit(Resource.failure(error.message.toString())) }
                    return@addSnapshotListener
                }

                val addresses=value?.toObjects(Address::class.java)
                viewModelScope.launch { _address.emit(Resource.success(addresses!!)) }
            }
    }
}