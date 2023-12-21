package com.example.application.fragments.loginRegister

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.application.R
import com.example.application.activities.ShoppingActivity
import com.example.application.databinding.FragmentLoginBinding
import com.example.application.dialog.setUpBottomSheetDialog
import com.example.application.util.Resource
import com.example.application.viewModel.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class fragmentlogin:Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            loginButton.setOnClickListener{
                val email=edtEmail.text.toString().trim()
                val password=edtPassword.text.toString()

                viewModel.loginWithEmailAndPass(email,password)
            }
        }
         binding.tvForgotPass.setOnClickListener {
             setUpBottomSheetDialog {email->
                 viewModel.resetPassword(email)
             }

             binding.tvDontHaveAnAccount.setOnClickListener {
                 findNavController().navigate(R.id.action_fragmentlogin_to_fragmentRegister)
             }
         }
        lifecycleScope.launchWhenStarted {
            viewModel.login.collect(){
                when(it){
                    is Resource.success->{
                        Intent(requireActivity(),ShoppingActivity::class.java).also {intent->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }
                    is Resource.failure->{
                        Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_SHORT).show()
                    }
                    else->Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.resetpassword.collect(){
                when(it){
                    is Resource.success->{
                    Snackbar.make(requireView(),"Reset link is sent to your email",Snackbar.LENGTH_SHORT).show()
                    }
                    is Resource.failure->{
                        Snackbar.make(requireView(),"Reset link is sent to your email",Snackbar.LENGTH_SHORT).show()
                    }
                    else->Unit
                }
            }
        }
    }
}