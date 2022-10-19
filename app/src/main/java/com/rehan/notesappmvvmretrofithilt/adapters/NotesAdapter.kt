package com.rehan.notesappmvvmretrofithilt.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rehan.notesappmvvmretrofithilt.databinding.AdapterNotesItemBinding
import com.rehan.notesappmvvmretrofithilt.models.NotesResponse

class NotesAdapter(private val onNotesClicked: (NotesResponse) -> Unit) : androidx.recyclerview.widget.ListAdapter<NotesResponse, NotesAdapter.ViewHolder>(ComparatorDiffUtil()) {

    // List adapter has only 2 inbuilt methods
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterNotesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = getItem(position)
        note?.let {
            holder.bind(it)
        }
    }

    inner class ViewHolder(private val binding: AdapterNotesItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(notesResponse: NotesResponse) {
            binding.tvTitle.text = notesResponse.title
            binding.tvDescription.text = notesResponse.description
            // When user click on view items, it will take user to next view page
            binding.root.setOnClickListener { 
                onNotesClicked(notesResponse)
            }
        }
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<NotesResponse>() {
        override fun areItemsTheSame(oldItem: NotesResponse, newItem: NotesResponse): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: NotesResponse, newItem: NotesResponse): Boolean {
            return oldItem == newItem
        }

    }
}