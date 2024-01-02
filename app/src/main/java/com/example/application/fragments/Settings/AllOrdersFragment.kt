package com.example.application.fragments.Settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.application.adapters.AllOrderAdapter
import com.example.application.databinding.FragmentOrdersBinding
import com.example.application.util.Resource
import com.example.application.viewModel.AllOrdersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class AllOrdersFragment:Fragment() {
    private lateinit var binding:FragmentOrdersBinding
    private val viewModel by viewModels<AllOrdersViewModel>()
    val orderAdapter by lazy { AllOrderAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentOrdersBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpAllOrdersrv()

        lifecycleScope.launchWhenStarted {
            viewModel.getAllOrders.collectLatest {
                when(it){
                    is Resource.loading->{
                        binding.progressbarAllOrders.visibility=View.VISIBLE
                    }
                    is Resource.success->{
                       binding.progressbarAllOrders.visibility=View.GONE
                        orderAdapter.differ.submitList(it.data)
                        if(it.data.isNullOrEmpty()){
                            binding.tvEmptyOrders.visibility=View.VISIBLE
                        }
                    }
                    is Resource.failure->{
                       Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_SHORT).show()
                    }
                    else ->Unit
                }
            }
        }

        orderAdapter.onClick={
            val action =AllOrdersFragmentDirections.actionAllOrdersFragmentToAllOrderDetailsFragment(it)
            findNavController().navigate(action)
        }
    }

    private fun setUpAllOrdersrv() {
        binding.rvAllOrders.apply {
            adapter=orderAdapter
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        }
    }
}