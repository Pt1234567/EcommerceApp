package com.example.application.fragments.category

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.application.data.Category
import com.example.application.util.Resource
import com.example.application.viewModel.Factory.BaseCategoryViewModelFactory
import com.example.application.viewModel.categoryViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BooksFragment:BaseCategoryFragment() {

    @Inject
    lateinit var firestore: FirebaseFirestore

   val viewModel by viewModels<categoryViewModel> {
       BaseCategoryViewModelFactory(firestore,Category.books)
   }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.offerproducts.collectLatest {
                when(it){

                    is Resource.loading->{

                    }

                    is Resource.success-> {
                        offerAdapter.differ.submitList(it.data)
                    }
                    is Resource.failure->{
                        Snackbar.make(requireView(),it.message.toString(),Snackbar.LENGTH_SHORT).show()
                    }

                    else ->Unit
                }
            }
        }

        lifecycleScope.launch {
            viewModel.bestproducts.collectLatest {
                when(it){

                    is Resource.loading->{
                      binding.baseProgress.visibility=View.VISIBLE
                    }

                    is Resource.success-> {
                        binding.baseProgress.visibility=View.INVISIBLE
                        bestProductsAdapter.differ.submitList(it.data)

                    }
                    is Resource.failure->{
                        binding.baseProgress.visibility=View.INVISIBLE
                        Snackbar.make(requireView(),it.message.toString(),Snackbar.LENGTH_SHORT).show()
                    }

                    else ->Unit
                }
            }
        }
    }
}