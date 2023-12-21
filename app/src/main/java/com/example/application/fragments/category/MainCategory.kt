package com.example.application.fragments.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.application.R
import com.example.application.adapters.BestDealsAdapter
import com.example.application.adapters.BestProductsAdapter
import com.example.application.adapters.HomeViewPager2Adapter
import com.example.application.adapters.specialProductAdapter
import com.example.application.databinding.FragmentHomeBinding
import com.example.application.databinding.FragmentMainCategoryBinding
import com.example.application.databinding.SpecialRvItemBinding
import com.example.application.util.Resource
import com.example.application.util.ShowBottomNavigation
import com.example.application.viewModel.MainCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.checkerframework.checker.units.qual.s

@AndroidEntryPoint
class MainCategory:Fragment() {
    private lateinit var binding: FragmentMainCategoryBinding
    private lateinit var specialProductAdapter: specialProductAdapter
    private lateinit var bestDealsAdapter:BestDealsAdapter
    private lateinit var bestProductsAdapter: BestProductsAdapter

    private val viewModel by viewModels<MainCategoryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding= FragmentMainCategoryBinding.inflate(inflater)
        return binding.root
    }

    private fun showLoading() {
        binding.progressBar.visibility=View.VISIBLE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpSpecialrv()
        setUpBestrv()
        setUpBestProducts()


        specialProductAdapter.onClick={
            val b=Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.action_homeFragment2_to_productDetailsFragment,b)
        }

        bestDealsAdapter.onClick={
            val b=Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.action_homeFragment2_to_productDetailsFragment,b)
        }

        bestProductsAdapter.onClick={
            val b=Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.action_homeFragment2_to_productDetailsFragment,b)
        }


        lifecycleScope.launch {
        viewModel.specialProduct.collectLatest {
            when(it){
                is Resource.loading->{
                    showLoading()
                }
                is Resource.success->{
                   specialProductAdapter.differ.submitList(it.data)
                    hideLoading()
                }
                is Resource.failure->{
                    hideLoading()
                    Toast.makeText(requireContext(),"Error occured",Toast.LENGTH_SHORT).show()

                }
                else->Unit
            }
        }
        }

        lifecycleScope.launch {
            viewModel.bestProduct.collectLatest {
                when(it){
                    is Resource.loading->{
                        binding.bottomProgressBar.visibility=View.VISIBLE
                    }
                    is Resource.success->{
                        bestProductsAdapter.differ.submitList(it.data)
                        binding.bottomProgressBar.visibility=View.INVISIBLE
                    }
                    is Resource.failure->{
                        binding.bottomProgressBar.visibility=View.INVISIBLE
                        Toast.makeText(requireContext(),"Error occured",Toast.LENGTH_SHORT).show()

                    }
                    else->Unit
                }
            }
        }

        lifecycleScope.launch {
            viewModel.dealsProduct.collectLatest {
                when(it){
                    is Resource.loading->{
                        showLoading()
                    }
                    is Resource.success->{
                        bestDealsAdapter.differ.submitList(it.data)
                        hideLoading()
                    }
                    is Resource.failure->{
                        hideLoading()
                        Toast.makeText(requireContext(),"Error occured",Toast.LENGTH_SHORT).show()

                    }
                    else->Unit
                }
            }
        }

        binding.nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener{v,_,ScrollY,_,_ ->
            if(v.getChildAt(0).bottom<=v.height+ScrollY){
                viewModel.fetchbestProducts()
            }
        })
    }

    private fun setUpBestProducts() {
        bestProductsAdapter= BestProductsAdapter()
        binding.productsRv.apply {
            layoutManager=GridLayoutManager(requireContext(),3,GridLayoutManager.VERTICAL,false)
            adapter=bestProductsAdapter
        }
    }

    private fun setUpBestrv() {
        bestDealsAdapter= BestDealsAdapter()
        binding.bestRv.apply {
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter=bestDealsAdapter
        }
    }

    private fun hideLoading() {
          binding.progressBar.visibility=View.GONE
      }

    private fun setUpSpecialrv() {
        specialProductAdapter= specialProductAdapter()
        binding.specialRv.apply {
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter=specialProductAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        ShowBottomNavigation()
    }
}