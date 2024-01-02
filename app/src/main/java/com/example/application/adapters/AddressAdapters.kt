package com.example.application.adapters

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.RecyclerView
import com.example.application.R
import com.example.application.data.Address
import com.example.application.databinding.AddressRvItemBinding
import com.example.application.databinding.FragmentBillingBinding

class AddressAdapters:RecyclerView.Adapter<AddressAdapters.AddressViewHolder>() {

    inner class AddressViewHolder(val binding: AddressRvItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(address: Address, isSelected: Boolean) {
         binding.apply {
             buttonAddress.text=address.addressTitle

             if(isSelected){
                 buttonAddress.background=ColorDrawable(ContextCompat.getColor(itemView.context,R.color.blue))
             }else{
                 buttonAddress.background=ColorDrawable(ContextCompat.getColor(itemView.context,R.color.white))
             }
         }
        }

    }

    val diffCallBack=object :ItemCallback<Address>(){
        override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem.addressTitle== newItem.addressTitle && oldItem.name==newItem.name
        }

        override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem==newItem
        }

    }

    val differ=AsyncListDiffer(this,diffCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
       return AddressViewHolder(AddressRvItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var selectedAddress=-1
    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val address=differ.currentList[position]

        holder.bind(address,selectedAddress==position)

        holder.binding.buttonAddress.setOnClickListener{

            if(selectedAddress>=0) notifyItemChanged(selectedAddress)

                selectedAddress=holder.adapterPosition
                notifyItemChanged(selectedAddress)
                onClick?.invoke(address)

        }
    }

    var onClick:((Address)->Unit)?=null
}