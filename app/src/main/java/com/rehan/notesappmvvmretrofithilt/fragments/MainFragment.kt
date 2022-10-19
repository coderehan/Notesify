package com.rehan.notesappmvvmretrofithilt.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.Gson
import com.rehan.notesappmvvmretrofithilt.R
import com.rehan.notesappmvvmretrofithilt.adapters.NotesAdapter
import com.rehan.notesappmvvmretrofithilt.databinding.FragmentMainBinding
import com.rehan.notesappmvvmretrofithilt.models.NotesResponse
import com.rehan.notesappmvvmretrofithilt.utils.NetworkResult
import com.rehan.notesappmvvmretrofithilt.viewmodels.NotesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val notesViewModel by viewModels<NotesViewModel>()

    private lateinit var adapter: NotesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        adapter = NotesAdapter(::onNotesClicked)        // Passing onNotesClicked() function as parameter to NotesAdapter class

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindObservers()
        notesViewModel.getNotes()
        binding.rvNotes.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.rvNotes.adapter = adapter
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_noteFragment)
        }
    }

    private fun bindObservers() {
        notesViewModel.notesLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.visibility = View.GONE
            when(it){
                is NetworkResult.Success ->{
                    adapter.submitList(it.data)
                }
                is NetworkResult.Error ->{
                    Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_LONG).show()
                }
                is NetworkResult.Loading ->{
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        })
    }

    // Passing this function as a parameter in Adapter class
    private fun onNotesClicked(notesResponse: NotesResponse){
        // Passing data from one fragment to another fragment using bundle
        val bundle = Bundle()
        bundle.putString("note", Gson().toJson(notesResponse))      // As data is coming from API, we use Gson to convert data into java/kotlin objects. This Gson will return string
        findNavController().navigate(R.id.action_mainFragment_to_noteFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}