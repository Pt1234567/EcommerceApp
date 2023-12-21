package com.example.application.fragments.loginRegister

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.application.R
import com.example.application.databinding.FragmentAccountOptionBinding
import com.example.application.databinding.FragmentRegisterBinding

class fragmentAccountOption:Fragment() {
    private lateinit var binding:FragmentAccountOptionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentAccountOptionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginBtnOption.setOnClickListener{
            findNavController().navigate(R.id.action_fragmentAccountOption_to_fragmentlogin)
        }
        binding.registerBtnOption.setOnClickListener{
            findNavController().navigate(R.id.action_fragmentAccountOption_to_fragmentRegister)
        }
    }
}