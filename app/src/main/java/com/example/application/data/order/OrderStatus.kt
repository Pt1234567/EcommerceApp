package com.example.application.data.order

sealed class OrderStatus(val status:String){
    object Ordered:OrderStatus("Ordered")
    object Confirmed:OrderStatus("Confirmed")
    object Cancelled:OrderStatus("Cancelled")
    object Returned:OrderStatus("Returned")
    object Delivered:OrderStatus("Delivered")

    object Shipped:OrderStatus("Shipped")
}

fun getOrderStatus(status : String) :OrderStatus{

    return  when(status){
        "Ordered"->{
            OrderStatus.Ordered
        }
        "Cancelled"->{
            OrderStatus.Cancelled
        }
        "Confirmed"->{
            OrderStatus.Confirmed
        }
        "Shipped"->{
            OrderStatus.Shipped
        }
        "Delivered"->{
            OrderStatus.Delivered
        }

        else->OrderStatus.Returned
    }
}
