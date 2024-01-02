package com.example.application.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.application.data.Cart
import com.example.application.databinding.BillingProductsRvItemBinding

class BillingAddressAdapter:RecyclerView.Adapter<BillingAddressAdapter.BillingAddressViewHolder>() {

    inner class BillingAddressViewHolder(val binding: BillingProductsRvItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(billingproduct: Cart) {
            binding.apply {
                Glide.with(itemView).load(billingproduct.product.images[0]).into(imageCartProduct)
                tvProductCartName.text=billingproduct.product.name.toString()
                tvBillingProductQuantity.text=billingproduct.quantity.toString()

                binding.tvProductCartPrice.text=billingproduct.product.price.toString()
                billingproduct.product.offerPercentage?.let {
                    val remainingPercentage=1f-it
                    val newPriceAfterOffer=remainingPercentage*billingproduct.product.price
                    binding.tvProductCartPrice.text=String.format(newPriceAfterOffer.toString(),2)
                }
            }
        }

    }

    val diffCallBack=object :ItemCallback<Cart>(){
        override fun areItemsTheSame(oldItem: Cart, newItem: Cart): Boolean {
            return oldItem.product==newItem.product
        }

        override fun areContentsTheSame(oldItem: Cart, newItem: Cart): Boolean {
            return oldItem==newItem
        }

    }

    val differ=AsyncListDiffer(this,diffCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillingAddressViewHolder {
        return BillingAddressViewHolder(BillingProductsRvItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
       return differ.currentList.size
    }

    override fun onBindViewHolder(holder: BillingAddressViewHolder, position: Int) {
        val billingproduct=differ.currentList[position]

        holder.bind(billingproduct)
    }
}