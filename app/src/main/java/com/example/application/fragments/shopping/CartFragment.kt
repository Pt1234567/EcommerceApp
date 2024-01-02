package com.example.application.fragments.shopping

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.application.R
import com.example.application.adapters.CartProductAdapter
import com.example.application.databinding.FragmentCartBinding
import com.example.application.firebase.FirebaseCommon
import com.example.application.util.Resource
import com.example.application.viewModel.CartFragmentViewModel
import kotlinx.coroutines.flow.collectLatest

class CartFragment:Fragment() {
    private lateinit var binding:FragmentCartBinding
    private val cartAdapter by lazy { CartProductAdapter() }
    private val viewModel by activityViewModels<CartFragmentViewModel> ()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentCartBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpCartListAdapter()

        var totalPrice=0f
        lifecycleScope.launchWhenStarted {
            viewModel.productPrice.collectLatest {price->
                price?.let {
                    totalPrice=price
                    binding.totalAmountCart.text=price.toString().format(2)
                }

            }
        }

        cartAdapter.onProductsClick={
            val b=Bundle().apply { putParcelable("product",it.product) }
            findNavController().navigate(R.id.action_cartFragment_to_productDetailsFragment,b)
        }

        cartAdapter.onPlusClick={
            viewModel.changingQuantity(it,FirebaseCommon.QuantityChanging.INCREASING)
        }

        cartAdapter.onMinusClick={
            viewModel.changingQuantity(it,FirebaseCommon.QuantityChanging.DECREASING)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.cartProducts.collectLatest {

                when(it){
                    is Resource.loading->{
                        binding.linearProgressCart.visibility=View.VISIBLE
                    }
                    is Resource.success->{
                        binding.linearProgressCart.visibility=View.INVISIBLE
                        if(it.data!!.isEmpty()) {
                            showEmptyCart()
                        }
                        else{
                            hideEmptyCart()
                            cartAdapter.differ.submitList(it.data)
                        }
                    }
                    is Resource.failure->{
                        binding.linearProgressCart.visibility=View.INVISIBLE
                        Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_SHORT).show()
                    }
                    else ->Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.deleteproduct.collectLatest {
                val alertDialog=AlertDialog.Builder(requireContext()).apply {
                    setTitle("Delete cart Product")
                        setMessage("Do you really want to delete this item from cart")
                            setNegativeButton("cancel"){dialog,_->
                                dialog.dismiss()
                            }
                    setPositiveButton("Yes"){dialog,_->
                        viewModel.deleteCartProduct(it)
                        dialog.dismiss()
                    }
                }
                alertDialog.create()
                alertDialog.show()
            }
        }


        binding.checkOutButton.setOnClickListener {
            val action=CartFragmentDirections.actionCartFragmentToBillingFragment(totalPrice,cartAdapter.differ.currentList.toTypedArray())
            findNavController().navigate(action)
        }
    }

    private fun hideEmptyCart() {
        binding.layoutCartEmpty.visibility=View.INVISIBLE
        binding.totalContainer.visibility=View.VISIBLE
        binding.checkOutButton.visibility=View.VISIBLE
    }

    private fun showEmptyCart() {
        binding.layoutCartEmpty.visibility=View.VISIBLE
        binding.totalContainer.visibility=View.INVISIBLE
        binding.checkOutButton.visibility=View.INVISIBLE
    }

    private fun setUpCartListAdapter() {
        binding.rvCart.apply {
            layoutManager=LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
            adapter=cartAdapter
        }
    }
}