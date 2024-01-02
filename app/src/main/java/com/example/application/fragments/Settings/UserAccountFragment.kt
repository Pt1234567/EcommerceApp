package com.example.application.fragments.Settings

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.application.data.User
import com.example.application.databinding.FragmentUserAccountBinding
import com.example.application.dialog.setUpBottomSheetDialog
import com.example.application.util.Resource
import com.example.application.viewModel.UserProfileViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class UserAccountFragment:Fragment() {
   private lateinit var binding:FragmentUserAccountBinding
   private val viewModel by viewModels<UserProfileViewModel> ()
    private  var imageUri:Uri?=null
    private  lateinit var imageActivityResultLauncher:ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageActivityResultLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            imageUri=it.data?.data
            Glide.with(this).load(imageUri).error(ColorDrawable(Color.BLACK)).into(binding.imageUser)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentUserAccountBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.user.collectLatest {
                when(it){
                    is Resource.loading->{
                        showLoading()
                    }
                    is Resource.success->{
                        hideLoading()
                        showUSerData(it.data!!)

                    }
                    is Resource.failure->{
                        Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_SHORT).show()
                    }
                    else->Unit
                }
            }
        }


        lifecycleScope.launchWhenStarted {
            viewModel.updateInfo.collectLatest {
                when(it){
                    is Resource.loading->{
                        binding.progressbarAccount.visibility=View.VISIBLE
                    }
                    is Resource.success->{
                        binding.progressbarAccount.visibility=View.INVISIBLE
                        findNavController().navigateUp()
                    }
                    is Resource.failure->{
                        Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_SHORT).show()
                    }
                    else->Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.resetpassword.collectLatest {
                when(it){
                    is Resource.success->{
                        Snackbar.make(requireView(),"Reset Link is sent to your email",Snackbar.LENGTH_SHORT).show()
                    }
                    is Resource.failure->{
                        Snackbar.make(requireView(),"Error occured",Snackbar.LENGTH_SHORT).show()
                    }
                    else->Unit
                }
            }
        }

        binding.buttonSave.setOnClickListener {
            val firstName=binding.edFirstName.text.toString().trim()
            val lastName=binding.edLastName.text.toString().trim()
            val email=binding.edEmail.text.toString().trim()

            val user=User(firstName, lastName, email)
            viewModel.updateUserInfo(user,imageUri)
        }

        binding.imageEdit.setOnClickListener {
            val intent=Intent(Intent.ACTION_GET_CONTENT)
            intent.type="image/*"
            imageActivityResultLauncher.launch(intent)
        }

        binding.tvUpdatePassword.setOnClickListener {
            setUpBottomSheetDialog {  }
        }

    }

    private fun showUSerData(data: User) {

        binding.apply {
            Glide.with(this@UserAccountFragment).load(data.imagePath).into(imageUser)
            edFirstName.setText(data.firstName)
            edLastName.setText(data.lastName)
            edEmail.setText(data.email)
        }
    }

    private fun hideLoading() {
        binding.apply {
            progressbarAccount.visibility=View.INVISIBLE
            imageUser.visibility=View.VISIBLE
            imageEdit.visibility=View.VISIBLE
            edFirstName.visibility=View.VISIBLE
            edLastName.visibility=View.VISIBLE
            edEmail.visibility=View.VISIBLE
            tvUpdatePassword.visibility=View.VISIBLE
            buttonSave.visibility=View.VISIBLE
        }
    }

    private fun showLoading() {
        binding.apply {
            progressbarAccount.visibility=View.VISIBLE
            imageUser.visibility=View.INVISIBLE
            imageEdit.visibility=View.INVISIBLE
            edFirstName.visibility=View.INVISIBLE
            edLastName.visibility=View.INVISIBLE
            edEmail.visibility=View.INVISIBLE
            tvUpdatePassword.visibility=View.INVISIBLE
            buttonSave.visibility=View.INVISIBLE
        }
    }
}