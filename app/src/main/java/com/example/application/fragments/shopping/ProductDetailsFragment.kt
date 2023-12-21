package com.example.application.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.application.adapters.ViewPager2Images
import com.example.application.data.Cart
import com.example.application.databinding.ProductDetailsFragmentBinding
import com.example.application.util.Resource
import com.example.application.util.hideBottomNavigation
import com.example.application.viewModel.AddToCartViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProductDetailsFragment:Fragment() {

     private val args by navArgs<ProductDetailsFragmentArgs>()
    private lateinit var binding:ProductDetailsFragmentBinding
  private  val addToCartViewModel by viewModels<AddToCartViewModel>()
    private val viewPagerAdapter by lazy {
        ViewPager2Images()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=ProductDetailsFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product =args.product
       setViewPager()
        hideBottomNavigation()

        binding.addToCart.setOnClickListener {
            addToCartViewModel.addUpdateAddToCart(Cart(product,1,null))
        }

        lifecycleScope.launchWhenStarted {
            addToCartViewModel.addToCart.collectLatest {
                when(it){
                    is Resource.loading ->
                    {

                    }
                    is Resource.success->{
                        Snackbar.make(requireView(),"Item Added to cart",Snackbar.LENGTH_LONG).show()
                    }
                    is Resource.failure->{
                      Toast.makeText(requireContext(),"Error while adding item",Toast.LENGTH_LONG).show()
                      }

                    else->Unit
                }
            }
        }

        binding.apply {
            tvProductname.text=product.name
            tvProductprice.text="$ ${product.price}"
            tvProductdescription.text=product.description
        }

   viewPagerAdapter.differ.submitList(product.images)
    }

    private fun setViewPager() {
        binding.apply {
            ProductImagesViewpager.adapter=viewPagerAdapter

        }
    }
}