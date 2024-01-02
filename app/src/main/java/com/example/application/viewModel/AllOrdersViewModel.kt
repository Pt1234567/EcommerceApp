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
class AllOrdersViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) :ViewModel(){

    private val _getAllOrders=MutableStateFlow<Resource<List<Order>>>(Resource.unspeciFied())
    val getAllOrders=_getAllOrders.asStateFlow()

    init {
        getAllOrder()
    }
    fun getAllOrder(){
        viewModelScope.launch {
            _getAllOrders.emit(Resource.loading())
        }

        firestore.collection("user").document(firebaseAuth.uid!!).collection("orders").get()
            .addOnSuccessListener {
                val orders=it.toObjects(Order::class.java)
                viewModelScope.launch {
                    _getAllOrders.emit(Resource.success(orders))
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _getAllOrders.emit(Resource.failure(it.message.toString()))
                }
            }
    }
}