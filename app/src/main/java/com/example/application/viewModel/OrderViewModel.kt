package com.example.application.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.application.data.order.Order
import com.example.application.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
):ViewModel() {

    private val _order=MutableStateFlow<Resource<Order>>(Resource.unspeciFied())

    val order=_order.asStateFlow()

    fun placeOrder(order:Order){
        viewModelScope.launch { _order.emit(Resource.loading()) }

        firestore.runBatch {
            //TODO: Add orders in user order collection
            //TODO: Add order in orders
            //TODO: Delete the products from user cart collections


            firestore.collection("user").document(firebaseAuth.uid!!).collection("orders")
                .document().set(order)

            firestore.collection("orders").document().set(order)

            firestore.collection("user").document(firebaseAuth.uid!!).collection("cart").get()
                .addOnSuccessListener {
                    it.documents.forEach{
                        it.reference.delete()
                    }
                }
        }.addOnSuccessListener{
            viewModelScope.launch {
                _order.emit(Resource.success(order))
            }
        }.addOnFailureListener {
            viewModelScope.launch {
                _order.emit(Resource.failure(it.message.toString() ))
            }
        }
    }
}