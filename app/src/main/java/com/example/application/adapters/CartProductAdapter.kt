package com.example.application.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.application.data.Cart
import com.example.application.databinding.ActivityShoppingBinding
import com.example.application.databinding.CartProductItemBinding

class CartProductAdapter:RecyclerView.Adapter<CartProductAdapter.CartproductViewHolder>() {

    inner class CartproductViewHolder(val binding:CartProductItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(cartProduct: Cart) {
          Glide.with(itemView).load(cartProduct.product.images[0]).into(binding.cartProductImage)
            binding.cartProductName.text=cartProduct.product.name
            binding.quantity.text=cartProduct.quantity.toString()
            cartProduct.product.offerPercentage?.let {
                val remainingPercentage=1f-it
                val newPriceAfterOffer=remainingPercentage*cartProduct.product.price
                binding.cartProductOfferprice.text=newPriceAfterOffer.toString()
                binding.cartProductPrice.paintFlags=Paint.STRIKE_THRU_TEXT_FLAG
            }
            if(cartProduct.product.offerPercentage==null){
                binding.cartProductOfferprice.visibility= View.GONE
            }
        }
    }

    val diffCallBack=object :DiffUtil.ItemCallback<Cart>(){
        override fun areItemsTheSame(oldItem: Cart, newItem: Cart): Boolean {
            return oldItem.product.id==newItem.product.id

        }

        override fun areContentsTheSame(oldItem: Cart, newItem: Cart): Boolean {
            return oldItem==newItem
        }

    }
   val differ=AsyncListDiffer(this,diffCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartproductViewHolder {
       return CartproductViewHolder(CartProductItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
       return differ.currentList.size
    }

    override fun onBindViewHolder(holder: CartproductViewHolder, position: Int) {
        val cartProduct=differ.currentList[position]

        holder.bind(cartProduct)

        holder.itemView.setOnClickListener {
            onProductsClick?.invoke(cartProduct)
        }
        holder.binding.plusCart.setOnClickListener {
            onPlusClick?.invoke(cartProduct)
        }

        holder.binding.minusCart.setOnClickListener {
            onMinusClick?.invoke(cartProduct)
        }
    }

    var onProductsClick:((Cart)->Unit)?=null
    var onPlusClick:((Cart)->Unit)?=null
    var onMinusClick:((Cart)->Unit)?=null
}