package com.example.application.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.application.data.Product
import com.example.application.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainCategoryViewModel @Inject constructor(
    private val firestore:FirebaseFirestore
):ViewModel() {

    private val _specialProduct=MutableStateFlow<Resource<List<Product>>>(Resource.unspeciFied())
    val specialProduct:StateFlow<Resource<List<Product>>> = _specialProduct

    private val _dealsProduct=MutableStateFlow<Resource<List<Product>>>(Resource.unspeciFied())
    val dealsProduct:StateFlow<Resource<List<Product>>> = _dealsProduct

    private val _bestProduct=MutableStateFlow<Resource<List<Product>>>(Resource.unspeciFied())
    val bestProduct:StateFlow<Resource<List<Product>>> = _bestProduct

    private  val pagingInfo=PagingInfo()

    init {
        fetchSpecialProduct()
        fetchbestProducts()
        fetchbestDealsProduct()
    }

     fun fetchbestProducts() {

         if(!pagingInfo.isPagingEnd) {
             viewModelScope.launch {
                 _bestProduct.emit(Resource.loading())
             }
             firestore.collection("Products").whereEqualTo("category", "products")
                 .limit(pagingInfo.page * 10).get().addOnSuccessListener { result ->
                 val bestproductList = result.toObjects(Product::class.java)
                 pagingInfo.isPagingEnd = bestproductList == pagingInfo.oldBestProducts
                 pagingInfo.oldBestProducts = bestproductList
                 viewModelScope.launch {
                     _bestProduct.emit(Resource.success(bestproductList))
                 }
                 pagingInfo.page++
             }.addOnFailureListener {
                 viewModelScope.launch {
                     _bestProduct.emit(Resource.failure(it.message.toString()))
                 }
             }
         }
    }

     fun fetchbestDealsProduct() {
        viewModelScope.launch {
            _dealsProduct.emit(Resource.loading())
        }

        firestore.collection("Products").whereEqualTo("category","deals").get().addOnSuccessListener {result->
            viewModelScope.launch {
                val bestList=result.toObjects(Product::class.java)
                _dealsProduct.emit(Resource.success(bestList))
            }
        }.addOnFailureListener {
            viewModelScope.launch {
                _dealsProduct.emit(Resource.failure(it.message.toString()))
            }
        }
    }

    fun fetchSpecialProduct(){
        viewModelScope.launch {
            _specialProduct.emit(Resource.loading())
        }

        firestore.collection("Products").whereEqualTo("category","Special").get().addOnSuccessListener {result->

            val specialProductsList=result.toObjects(Product::class.java)

            viewModelScope.launch {
                    _specialProduct.emit(Resource.success(specialProductsList))
            }
        }
            .addOnFailureListener {
               viewModelScope.launch{
                   _specialProduct.emit(Resource.failure(it.message.toString()))
               }
            }
    }

    internal data class PagingInfo(
        //paging is use to fetch data from database in smal crunch ,because if 1000s of items are present in database fetching all together will be slow
        var page:Long=1,
        var oldBestProducts:List<Product> = emptyList(),
        var isPagingEnd: Boolean=false
    )
}