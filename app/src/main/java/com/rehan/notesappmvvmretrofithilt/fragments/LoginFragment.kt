package com.rehan.notesappmvvmretrofithilt.fragments

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rehan.notesappmvvmretrofithilt.R
import com.rehan.notesappmvvmretrofithilt.databinding.FragmentLoginBinding
import com.rehan.notesappmvvmretrofithilt.models.UserRequest
import com.rehan.notesappmvvmretrofithilt.utils.NetworkResult
import com.rehan.notesappmvvmretrofithilt.utils.TokenManager
import com.rehan.notesappmvvmretrofithilt.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Observer
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val userViewModel by viewModels<UserViewModel>()
    @Inject
    private lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignin.setOnClickListener {
            val validationResult = validateUserInput()
            if(validationResult.first){
                userViewModel.loginUser(getUserRequest())
            }else{
                binding.tvError.text = validationResult.second
            }
        }

        binding.tvSignup.setOnClickListener {
            // If we click tvSignUp in Login page, it will take us to Register page.
            // Now in Register page, if we click back button it will take us to Login page again according to inbuilt process of back stack in android
            // So inorder to prevent going back to Login page again, we use popBackStack() method to remove that page from top of stack
            findNavController().popBackStack()
        }

        bindObservers()
    }

    private fun getUserRequest(): UserRequest {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        return UserRequest(email, password, "")
    }

    private fun validateUserInput(): Pair<Boolean, String> {
        val userRequest = getUserRequest()
        return userViewModel.validateCredentials(userRequest.username, userRequest.email, userRequest.password, true)
    }

    private fun bindObservers() {
        userViewModel.userResponseLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            binding.progressBar.visibility = View.GONE
            when(it){
                is NetworkResult.Success -> {
                    // When we get success response in postman for login page, we will be getting response which has token as well. we will save that token response.
                    tokenManager.saveToken(it.data!!.token)
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                }
                is NetworkResult.Error -> {
                    binding.tvError.text = it.message
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}