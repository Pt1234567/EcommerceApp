package com.example.application.fragments.loginRegister

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.application.R
import com.example.application.activities.ShoppingActivity
import com.example.application.databinding.FragmentIntroductionBinding
import com.google.firebase.auth.FirebaseAuth

class fragmentIntroduction:Fragment() {
    private lateinit var binding: FragmentIntroductionBinding

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
        binding.apply {
            startButton.setOnClickListener{
                GotoShoppingActivityIfAlreadyLogin()
            }
        }
    }

    private fun GotoShoppingActivityIfAlreadyLogin() {
       if(FirebaseAuth.getInstance().currentUser!=null){
           Intent(requireActivity(), ShoppingActivity::class.java).also { intent->
               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
               startActivity(intent)
           }
       }else{
           findNavController().navigate(R.id.action_fragmentIntroduction_to_fragmentAccountOption)
       }
    }
}