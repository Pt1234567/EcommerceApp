package com.example.application.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.application.data.Product
import com.example.application.databinding.BestdealsRvItemBinding

class BestDealsAdapter:RecyclerView.Adapter<BestDealsAdapter.BaseDealsViewHolder>() {
    inner class BaseDealsViewHolder(val binding: BestdealsRvItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            Glide.with(itemView).load(product.images[0]).into(binding.bestImg)
            binding.bestName.text=product.name

            binding.bestPrice.text=product.price.toString()
                product.offerPercentage?.let {
                    val remainingPercentageoffer=1f-it
                    val priceAfteroffer=remainingPercentageoffer*product.price
                    binding.bestOfferPrice.text=priceAfteroffer.toString()
                }
            if(product.offerPercentage==null){
                binding.bestOfferPrice.visibility= View.GONE
            }

        }
    }

    val diffCallBack=object:DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
           return oldItem==newItem
        }

    }

    val differ=AsyncListDiffer(this,diffCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseDealsViewHolder {
        return BaseDealsViewHolder(BestdealsRvItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: BaseDealsViewHolder, position: Int) {
        val product=differ.currentList[position]
        holder.bind(product)

        holder.itemView.setOnClickListener {
            onClick?.invoke(product)
        }
    }

    var onClick:((Product)->Unit)?=null
}