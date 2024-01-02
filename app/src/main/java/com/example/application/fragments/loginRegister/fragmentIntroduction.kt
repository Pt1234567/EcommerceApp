package com.example.application.fragments.loginRegister

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.application.R
import com.example.application.activities.ShoppingActivity
import com.example.application.databinding.FragmentIntroductionBinding
import com.example.application.viewModel.IntroductionViewModel
import com.example.application.viewModel.IntroductionViewModel.Companion.ACCOUNT_OPTION_FRAGMENT
import com.example.application.viewModel.IntroductionViewModel.Companion.SHOPPING_ACTIVITY
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class fragmentIntroduction:Fragment() {
    private lateinit var binding: FragmentIntroductionBinding
    private val viewModel by viewModels<IntroductionViewModel> ()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentIntroductionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

          lifecycleScope.launchWhenStarted {
              viewModel.navigate.collect{
                  when(it){
                      SHOPPING_ACTIVITY->{
                          Intent(requireActivity(),ShoppingActivity::class.java).also { intent ->
                              intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                              startActivity(intent)
                          }
                      }
                      ACCOUNT_OPTION_FRAGMENT->{
                          findNavController().navigate(R.id.action_fragmentIntroduction_to_fragmentAccountOption)
                      }
                      else->Unit

                  }
              }
          }

        binding.startButton.setOnClickListener {
            viewModel.startButtonClick()
            findNavController().navigate(R.id.action_fragmentIntroduction_to_fragmentAccountOption)
        }

    }
}