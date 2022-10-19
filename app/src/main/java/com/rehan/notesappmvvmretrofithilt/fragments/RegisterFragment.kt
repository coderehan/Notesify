package com.rehan.notesappmvvmretrofithilt.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.rehan.notesappmvvmretrofithilt.R
import com.rehan.notesappmvvmretrofithilt.databinding.FragmentRegisterBinding
import com.rehan.notesappmvvmretrofithilt.models.UserRequest
import com.rehan.notesappmvvmretrofithilt.utils.NetworkResult
import com.rehan.notesappmvvmretrofithilt.utils.TokenManager
import com.rehan.notesappmvvmretrofithilt.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null   // Now our fragment binding object is nullable type

    // Inorder to make it non nullable we use one more variable and make it non nullable through get method by adding !!
    private val binding get() = _binding!!                  // !! means definitely it is not null (or) it means null safety

    // Using inbuilt viewModels() method, we don't need to initialize ViewModelProvider. This  inbuilt function will generate behind the scene
    // Calling ViewModel class here in fragment
    private val userViewModel by viewModels<UserViewModel>()

    @Inject
    private lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        // If we have token already, we will navigate user to main fragment directly
        if(tokenManager.getToken() != null){
            findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
        }

        return binding.root
    }

    // When view is created, we will bind our observers from view model
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignup.setOnClickListener {
            val validationResult = validateUserInput()          // Passing function to variable
            if(validationResult.first){                         // If first value i.e. boolean is true, we will execute this successful registration
                userViewModel.registerUser(getUserRequest())
            }else{
                binding.tvError.text = validationResult.second  // If first value boolean i.e. is false, we will return string statement
            }
        }

        binding.tvSignin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        bindObservers()

    }

    private fun getUserRequest(): UserRequest{
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val username = binding.etUsername.text.toString().trim()
        return UserRequest(email, password, username)
    }

    private fun validateUserInput(): Pair<Boolean, String> {
        val userRequest = getUserRequest()          // Passing function to variable
        return userViewModel.validateCredentials(userRequest.username, userRequest.email, userRequest.password, false)
    }

    private fun bindObservers() {
        userViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.visibility = View.GONE       // Hiding progress bar when we observe the data
            when(it){
                is NetworkResult.Success -> {
                    tokenManager.saveToken(it.data!!.token)      // Saving token when user sign up
                    findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
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


    // When view (fragment) is destroyed, it means it has nothing now. So we set our binding object also to null. When there is no view, data binding object should also be not there
    // When view created, data binding should also be created for that. when view destroyed, data binding object should not be destroyed for that
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}