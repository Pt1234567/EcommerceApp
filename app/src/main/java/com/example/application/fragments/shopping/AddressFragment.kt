package com.example.application.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.application.data.Address
import com.example.application.databinding.FragmentAddressBinding
import com.example.application.util.Resource
import com.example.application.viewModel.AddressViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class AddressFragment:Fragment() {
    private lateinit var binding: FragmentAddressBinding
    val viewModel by viewModels<AddressViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentAddressBinding.inflate(inflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.addAddress.collectLatest {
                when(it){
                    is Resource.loading->{
                        binding.progressbarAddress.visibility=View.VISIBLE
                    }
                    is Resource.success->{
                        binding.progressbarAddress.visibility=View.INVISIBLE
                        findNavController().navigateUp()
                    }
                    is Resource.failure->{
                        binding.progressbarAddress.visibility=View.INVISIBLE
                    }
                    else ->Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {

            viewModel.error.collectLatest {
                Toast.makeText(requireContext(),it,Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
      binding.apply {
          btnAddNewAddress.setOnClickListener{
              val addressTitle=edAddressTitle.text.toString()
              val fullname=edFullName.text.toString()
              val street=edStreet.text.toString()
              val phone=edPhone.text.toString()
              val state=edState.text.toString()
              val city=edCity.text.toString()

              val address=Address(addressTitle,fullname,street,phone, city, state)

              viewModel.addAddress(address)
          }
      }
    }
}