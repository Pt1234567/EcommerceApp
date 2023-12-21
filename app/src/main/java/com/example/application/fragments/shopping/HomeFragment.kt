package com.example.application.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.application.adapters.HomeViewPager2Adapter
import com.example.application.databinding.FragmentHomeBinding
import com.example.application.fragments.category.HomeAndKitchenFragment
import com.example.application.fragments.category.BooksFragment
import com.example.application.fragments.category.ClothingFragment
import com.example.application.fragments.category.MainCategory
import com.example.application.fragments.category.ElectronicsFragments
import com.example.application.fragments.category.MobileFragment
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment:Fragment() {
private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
           binding=FragmentHomeBinding.inflate(inflater)
           return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoriesList= arrayListOf<Fragment>(
            MainCategory(),
            BooksFragment(),
            MobileFragment(),
            ElectronicsFragments(),
            HomeAndKitchenFragment(),
            ClothingFragment()
        )

        val viewPager2Adapter=HomeViewPager2Adapter(categoriesList,childFragmentManager,lifecycle)
        binding.viewPager.adapter=viewPager2Adapter
        TabLayoutMediator(binding.tabLayout,binding.viewPager){tab,position->
            when(position){
                0->tab.text="Main"
                1->tab.text="Books"
                2->tab.text="Mobile"
                3->tab.text="Electronics"
                4->tab.text="Home&Kitchen"
                5->tab.text="Clothing"
            }
        }.attach()
        }
    }
