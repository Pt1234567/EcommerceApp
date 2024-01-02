package com.example.application.adapters

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.RecyclerView
import com.example.application.R
import com.example.application.data.order.Order
import com.example.application.data.order.OrderStatus
import com.example.application.data.order.getOrderStatus
import com.example.application.databinding.OrderItemBinding

class AllOrderAdapter:RecyclerView.Adapter<AllOrderAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(val binding:OrderItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(order: Order) {
         binding.apply {
             tvOrderDate.text=order.date
             tvOrderId.text=order.orderId.toString()

             val resources=itemView.resources

             val colorDrawable=when(getOrderStatus(order.orderStatus)){
                 is OrderStatus.Ordered->{
                     ColorDrawable(resources.getColor(R.color.yellow))
                 }

                 is OrderStatus.Confirmed->{
                     ColorDrawable(resources.getColor(R.color.green))
                 }
                 is OrderStatus.Delivered->{
                     ColorDrawable(resources.getColor(R.color.green))
                 }

                 is OrderStatus.Shipped->{
                     ColorDrawable(resources.getColor(R.color.green))
                 }
                 is OrderStatus.Cancelled->{
                     ColorDrawable(resources.getColor(R.color.red))
                 }
                 is OrderStatus.Returned->{
                     ColorDrawable(resources.getColor(R.color.red))
                 }


             }

             imageOrderState.setImageDrawable(colorDrawable)
         }
        }

    }

    private val diffCallBack=object :ItemCallback<Order>(){
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.products==newItem.products
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem==newItem
        }

    }

    val differ=AsyncListDiffer(this,diffCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder(OrderItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order=differ.currentList[position]
        holder.bind(order)

        holder.itemView.setOnClickListener {
            onClick?.invoke(order)
        }
    }

    var onClick: ((Order)->Unit)?=null
}