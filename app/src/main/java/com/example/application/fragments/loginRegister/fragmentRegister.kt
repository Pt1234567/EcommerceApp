package com.example.application.fragments.loginRegister

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.application.R
import com.example.application.data.User
import com.example.application.databinding.FragmentRegisterBinding
import com.example.application.util.RegisterValidation
import com.example.application.util.Resource
import com.example.application.viewModel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class fragmentRegister:Fragment() {

    private val TAG="RegisterFragment"
    private lateinit var binding:FragmentRegisterBinding
    private  val viewModel by viewModels<RegisterViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            registerBtn.setOnClickListener {
                val user= User(
                    edtFirstName.text.toString().trim(),
                    edtLastName.text.toString().trim(),
                    edtEmailReg.text.toString().trim()
                )
                val password=edtPassReg.text.toString()
                viewModel.createAccountWithEmailAndPassword(user,password)

            }

        }

        binding.alreadyHaveAccount.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentRegister_to_fragmentlogin)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.register.collect(){
                when(it){
//                    is Resource.loading->{
//                        //
//                    }
                    is Resource.success->{
                        Log.d("test",it.data.toString())
                        //Toast.makeText(context,"Account Created Successfully",Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_fragmentRegister_to_fragmentlogin2)
                    }
                    is Resource.failure->{
                        Log.e(TAG,it.message.toString())
                        Toast.makeText(context,"Error while creating account",Toast.LENGTH_SHORT).show()
                    }
                    else->Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.validate.collect(){validation->

                if(validation.email is RegisterValidation.failure){
                    withContext(Dispatchers.Main){
                        binding.edtEmailReg.apply {
                            requestFocus()
                            error=validation.email.message
                        }
                    }
                }

                if(validation.password is RegisterValidation.failure){
                    withContext(Dispatchers.Main){
                        binding.edtPassReg.apply {
                            requestFocus()
                            error=validation.password.message
                        }
                    }
                }
            }
        }


    }


}