package com.example.application.fragments.category

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.application.R
import com.example.application.data.Category
import com.example.application.util.Resource
import com.example.application.viewModel.Factory.BaseCategoryViewModelFactory
import com.example.application.viewModel.categoryViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class ClothingFragment:BaseCategoryFragment() {

    @Inject
    lateinit var firestore: FirebaseFirestore

    val viewModel by viewModels<categoryViewModel> {
        BaseCategoryViewModelFactory(firestore,Category.clothing)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        offerAdapter.onClick={
            val b=Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.action_homeFragment2_to_productDetailsFragment,b)
        }

        bestProductsAdapter.onClick={
            val b=Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.action_homeFragment2_to_productDetailsFragment,b)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.offerproducts.collectLatest {
                when(it){
                    is Resource.loading->{

                    }
                    is Resource.success->{
                        offerAdapter.differ.submitList(it.data)
                    }
                    is Resource.failure->{
                        Snackbar.make(requireView(),it.message.toString(), Snackbar.LENGTH_SHORT).show()
                    }
                    else-> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.bestproducts.collectLatest {
                when (it) {
                    is Resource.loading -> {

                    }

                    is Resource.success -> {
                        bestProductsAdapter.differ.submitList(it.data)
                    }

                    is Resource.failure -> {
                        Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_SHORT)
                            .show()
                    }

                    else -> Unit
                }
            }
        }
    }
}