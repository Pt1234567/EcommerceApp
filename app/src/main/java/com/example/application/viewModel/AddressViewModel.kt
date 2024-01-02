package com.example.application.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.application.data.Address
import com.example.application.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
):ViewModel() {

    private val _addAddress=MutableStateFlow<Resource<Address>>(Resource.unspeciFied())
    val addAddress=_addAddress.asStateFlow()

    private val _error=MutableSharedFlow<String>()
    val error=_error.asSharedFlow()
    fun addAddress(addresss: Address){

        val validateAddress=ValidateInput(addresss)
        if(validateAddress){
            viewModelScope.launch {
                _addAddress.emit(Resource.loading())
            }
           firestore.collection("user").document(firebaseAuth.uid!!).collection("address").document()
               .set(addresss).addOnSuccessListener {
                viewModelScope.launch {
                    _addAddress.emit(Resource.success(addresss))
                }
               }.addOnFailureListener {
                   viewModelScope.launch {
                       _addAddress.emit(Resource.failure(it.message.toString()))
                   }
               }
        }else
        {
                 viewModelScope.launch {
                     _error.emit("All field required")
                 }
        }

    }

    private fun ValidateInput(addresss: Address): Boolean {
         return addresss.addressTitle.trim().isNotEmpty() &&
                 addresss.city.trim().isNotEmpty()&&
                 addresss.name.trim().isNotEmpty()&&
                 addresss.phone.trim().isNotEmpty()&&
                 addresss.state.trim().isNotEmpty()&&
                 addresss.street.trim().isNotEmpty()
    }
}