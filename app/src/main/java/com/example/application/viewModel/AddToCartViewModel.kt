package com.example.application.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.application.data.Cart
import com.example.application.firebase.FirebaseCommon
import com.example.application.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddToCartViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseCommon: FirebaseCommon,
     private val auth: FirebaseAuth
):ViewModel() {

    private val _addToCart=MutableStateFlow<Resource<Cart>>(Resource.unspeciFied())
    val addToCart=_addToCart.asStateFlow()

    fun  addUpdateAddToCart(cart: Cart){
        firestore.collection("user").document(auth.uid!!).collection("cart")
            .whereEqualTo("product.id",cart.product.id).get().addOnSuccessListener { it ->
                it.documents.let {
                     if(it.isEmpty()){
                         //Add new Product
                         addNewProduct(cart)
                     }else{
                         val product=it.first().toObject(Cart::class.java)
                         if(product?.product?.id==cart.product.id){
                             //increase quantity
                             val documentId=it.first().id
                             increaseQuantity(documentId,cart)
                         }else{
                             //add new
                             addNewProduct(cart)
                         }
                     }
                 }
             }.addOnFailureListener {
              viewModelScope.launch {
                  _addToCart.emit(Resource.failure(it.message.toString()))
              }
            }
    }

    fun addNewProduct(cart: Cart){
        firebaseCommon.addProducts(cart){addedProduct,e->
            viewModelScope.launch {
                if(e==null){
                    _addToCart.emit(Resource.success(addedProduct!!))
                }else{
                    _addToCart.emit(Resource.failure(e.message.toString()))
                }
            }
        }
    }

    fun increaseQuantity(documentId:String,cart: Cart){
        firebaseCommon.increaseQuantity(documentId){_,e->

            viewModelScope.launch {
                if(e==null){
                    _addToCart.emit(Resource.success(cart))
                }else{
                    _addToCart.emit((Resource.failure(e.message.toString())))
                }
            }

        }
    }
}