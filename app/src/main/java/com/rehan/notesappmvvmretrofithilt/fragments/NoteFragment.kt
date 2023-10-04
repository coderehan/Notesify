package com.rehan.notesappmvvmretrofithilt.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.rehan.notesappmvvmretrofithilt.databinding.FragmentNoteBinding
import com.rehan.notesappmvvmretrofithilt.models.notes.NotesRequest
import com.rehan.notesappmvvmretrofithilt.models.notes.NotesResponse
import com.rehan.notesappmvvmretrofithilt.utils.NetworkResult
import com.rehan.notesappmvvmretrofithilt.viewmodels.NotesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteFragment : Fragment() {

    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!
    private var notesResponse: NotesResponse? = null
    private val notesViewModel by viewModels<NotesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInitialData()
        bindHandlers()
        bindObservers()
    }

    private fun bindHandlers() {
        binding.ivDeleteNotes.setOnClickListener {
            notesResponse?.let {
                notesViewModel.deleteNotes(it!!._id)
            }
        }

        binding.btnSubmit.setOnClickListener {
            val title = binding.etTitle.toString().trim()
            val description = binding.etDescription.toString().trim()
            val notesRequest = NotesRequest(description, title)

            if(notesResponse == null){
                notesViewModel.createNotes(notesRequest)
            }else{
                notesViewModel.updateNotes(notesResponse!!._id, notesRequest)
            }
        }
    }

    private fun bindObservers() {
        notesViewModel.statusLiveData.observe(viewLifecycleOwner, Observer {
            when (it){
                is NetworkResult.Success ->{
                    findNavController().popBackStack()
                }
                is NetworkResult.Error ->{

                }
                is NetworkResult.Loading ->{

                }
            }
        })
    }


    private fun setInitialData() {
        // Getting data from source fragment where we pass data and displaying in this destination fragment
        // We use arguments to get data from bundle
        val jsonNotes = arguments?.getString("note")
        if(jsonNotes != null){      // If it is not null, it means it belongs to updated view. Just show the data in the edittext.
            notesResponse = Gson().fromJson(jsonNotes, NotesResponse::class.java)   // Deserializing
            notesResponse?.let {
                binding.etTitle.setText(it.title)
                binding.etDescription.setText(it.description)
            }
        }else{      // If it is null, it means we have to add new notes
            binding.tvHeader.text = "Add Notes"
            binding.ivDeleteNotes.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}