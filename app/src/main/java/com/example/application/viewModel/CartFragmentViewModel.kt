package com.example.application.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.application.data.Cart
import com.example.application.firebase.FirebaseCommon
import com.example.application.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CartFragmentViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val  firebaseCommon: FirebaseCommon,
    private  val firestore: FirebaseFirestore
):ViewModel(){

    private val _carProducts= MutableStateFlow<Resource<List<Cart>>>(Resource.unspeciFied())
    val cartProducts=_carProducts.asStateFlow()
    private var cartProductDocuments= emptyList<DocumentSnapshot>()

    fun getProducts(){
        viewModelScope.launch {
            _carProducts.emit(Resource.loading())
        }

        firestore.collection("user").document(firebaseAuth.uid!!).collection("cart").addSnapshotListener{
            value,e->
            if(e!=null || value==null){
                viewModelScope.launch { _carProducts.emit(Resource.failure(e?.message.toString())) }
            }else{
                cartProductDocuments=value.documents
                val cartProducts=value.toObjects(Cart::class.java)

                viewModelScope.launch { _carProducts.emit(Resource.success(cartProducts)) }
            }
        }



        fun increaseCount(documentId: String) {
            firebaseCommon.increaseQuantity(documentId){result,exception->
                if(exception!=null){
                    viewModelScope.launch{_carProducts.emit(Resource.failure(exception.message.toString()))}
                }
            }
        }

        fun decreaseCount(documentId: String) {
            firebaseCommon.decreaseQuantity(documentId){result,exception->
                if(exception!=null){
                    viewModelScope.launch{_carProducts.emit(Resource.failure(exception.message.toString()))}
                }
            }
        }

        fun changingQuantity(
            cart: Cart,
            quantityChanging: FirebaseCommon.QuantityChanging
        ){
              val index=cartProducts.value.data?.indexOf(cart)
               if(index!=null && index!=-1) {
                   val documentId=cartProductDocuments[index].id


                   when(quantityChanging){
                       FirebaseCommon.QuantityChanging.INCREASING->{
                           increaseCount(documentId)
                       }
                       FirebaseCommon.QuantityChanging.DECREASING->{
                           decreaseCount(documentId)
                       }
                   }
               }
        }


    }

}