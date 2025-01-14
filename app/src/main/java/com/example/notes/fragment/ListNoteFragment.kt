package com.example.notes.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notes.adapter.Adapter
import com.example.notes.data.Note
import com.example.notes.databinding.FragmentNoteListBinding

class ListNoteFragment : Fragment() {

    private var _binding: FragmentNoteListBinding? = null
    private val binding get() = _binding!!

    private val notes = mutableListOf<Note>()

    private lateinit var adapter: Adapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = Adapter(notes)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        parentFragmentManager.setFragmentResultListener("add_note", viewLifecycleOwner) { _, bundle ->
            val newNote = bundle.getParcelable<Note>("note")
            newNote?.let {
                notes.add(it)
                adapter.notifyItemInserted(notes.size - 1)
            }
        }

        binding.fabAddNote.setOnClickListener {
            val action = ListNoteFragmentDirections.actionNoteListFragmentToAddNoteFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
