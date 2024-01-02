package com.example.application.fragments.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.application.R
import com.example.application.adapters.BestProductsAdapter
import com.example.application.databinding.FragmentBaseCategoryBinding

open class BaseCategoryFragment:Fragment() {
     protected lateinit var binding: FragmentBaseCategoryBinding
    protected  val offerAdapter:BestProductsAdapter by lazy {BestProductsAdapter()}
    protected val bestProductsAdapter: BestProductsAdapter by lazy { BestProductsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentBaseCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOfferrv()
        setUpbestrv()


        binding.baseProductRv.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(!recyclerView.canScrollVertically(1) && dx!=0){
                    onOfferpagingRequest()
                }
            }
        })

        binding.baseCategoryNested.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener{ v, _, ScrollY, _, _ ->
            if(v.getChildAt(0).bottom<=v.height+ScrollY){
                 onbestpagingRequest()
             }
        })
    }

    open fun onOfferpagingRequest(){

    }

    open fun onbestpagingRequest(){

    }

    private fun setUpbestrv() {
        binding.baseBestProductsRv.apply {
            layoutManager= GridLayoutManager(requireContext(),3, GridLayoutManager.VERTICAL,false)
            adapter=bestProductsAdapter
        }
    }

    private fun setupOfferrv() {
        binding.baseProductRv.apply {
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter=offerAdapter
        }
    }
}