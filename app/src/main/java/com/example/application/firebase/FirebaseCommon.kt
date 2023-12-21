package com.example.application.firebase

import com.example.application.data.Cart
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import java.lang.Exception

class FirebaseCommon(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,

) {

    private val cartCollection=firestore.collection("user").document(auth.uid!!).collection("cart")

    fun addProducts(cart: Cart,onResult:(Cart?,Exception?)->Unit){
        cartCollection.document().set(cart).addOnSuccessListener {
            onResult(cart,null)
        }
            .addOnFailureListener {
                onResult(null,it)
            }
    }

    fun increaseQuantity(documentId:String,onResult: (String?, Exception?) -> Unit){
        firestore.runTransaction {transaction->
            val documentRef=cartCollection.document(documentId)
            val document=transaction.get(documentRef)
            val productObj=document.toObject(Cart::class.java)

            productObj?.let {cart->
                cart.quantity++
                val newProductObject=cart.copy(quantity = cart.quantity)
                transaction.set(documentRef,newProductObject)
            }
        }.addOnSuccessListener {
             onResult(documentId,null)
        }.addOnFailureListener {
            onResult(null,it)
        }
    }

    fun decreaseQuantity(documentId:String,onResult: (String?, Exception?) -> Unit){
        firestore.runTransaction {transaction->
            val documentRef=cartCollection.document(documentId)
            val document=transaction.get(documentRef)
            val productObj=document.toObject(Cart::class.java)

            productObj?.let {cart->
                val newQuantity=cart.quantity-1
                val newProductObject=cart.copy(quantity = newQuantity)
                transaction.set(documentRef,newProductObject)
            }
        }.addOnSuccessListener {
            onResult(documentId,null)
        }.addOnFailureListener {
            onResult(null,it)
        }
    }
    enum class QuantityChanging{
        INCREASING,DECREASING
    }
}