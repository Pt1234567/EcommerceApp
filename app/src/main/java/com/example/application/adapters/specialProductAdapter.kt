package com.example.application.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.application.data.Product
import com.example.application.databinding.SpecialRvItemBinding

class specialProductAdapter:RecyclerView.Adapter<specialProductAdapter.SpecialProductViewHolder>() {
    inner class  SpecialProductViewHolder(val binding:SpecialRvItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            if (product.images.isNotEmpty()) {
                Glide.with(itemView).load(product.images[0]).into(binding.specialImg)
            }
            binding.specialName.text=product.name
            binding.specialPrice.text=product.price.toString()
        }
    }

    val diffCallback= object :DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem==newItem
        }

    }

    val differ=AsyncListDiffer(this,diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialProductViewHolder {
       return  SpecialProductViewHolder(
           SpecialRvItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
       )
     }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: SpecialProductViewHolder, position: Int) {
        val product=differ.currentList[position]
        holder.bind(product)

        holder.itemView.setOnClickListener {
            onClick?.invoke(product)
        }
    }

    var onClick:((Product)->Unit)?=null
}