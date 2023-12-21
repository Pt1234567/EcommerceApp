package com.example.application.viewModel

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.application.data.Category
import com.example.application.data.Product
import com.example.application.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class categoryViewModel constructor(
    private val firestore: FirebaseFirestore,
    private val category: Category
) :ViewModel(){


    private val _offerproducts=MutableStateFlow<Resource<List<Product>>>(Resource.unspeciFied())
    val offerproducts=_offerproducts.asStateFlow()

  private   val _Bestproducts=MutableStateFlow<Resource<List<Product>>>(Resource.unspeciFied())
    val bestproducts=_Bestproducts.asStateFlow()


    init {
        fetchBestProducts()
        fetchOfferProducts()
    }

    fun fetchOfferProducts(){
        firestore.collection("Products").whereEqualTo("category",category.category)
            .whereNotEqualTo("offerPercentage",null).get().addOnSuccessListener {
                val productsList=it.toObjects(Product::class.java)
                viewModelScope.launch {
                    _offerproducts.emit(Resource.success(productsList))
                }
            }.addOnFailureListener {
               viewModelScope.launch {
                   _offerproducts.emit(Resource.failure(it.message.toString()))
               }
            }
    }

    fun fetchBestProducts(){
        firestore.collection("Products").whereEqualTo("category",category.category)
            .whereEqualTo("offerPercentage",null).get().addOnSuccessListener {
                val productsList=it.toObjects(Product::class.java)
                viewModelScope.launch {
                    _Bestproducts.emit(Resource.success(productsList))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _Bestproducts.emit(Resource.failure(it.message.toString()))
                }
            }
    }
}