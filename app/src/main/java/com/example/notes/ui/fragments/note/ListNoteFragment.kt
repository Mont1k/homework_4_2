package com.example.notes.ui.fragments.note

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notes.ui.adapters.Adapter
import com.example.notes.R
import com.example.notes.data.model.Note
import com.example.notes.databinding.FragmentNoteListBinding
import com.example.notes.`interface`.OnClickItem
import com.google.firebase.firestore.FirebaseFirestore

class ListNoteFragment : Fragment(), OnClickItem {

    private lateinit var binding: FragmentNoteListBinding
    private lateinit var noteAdapter: Adapter
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteAdapter = Adapter(this, this)
        initializeRecyclerView()
        setupListener()
        getDataFromFirestore()
    }

    private fun initializeRecyclerView() {
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = noteAdapter
    }

    private fun setupListener() {
        binding.fabAddNote.setOnClickListener {
            findNavController().navigate(R.id.action_noteListFragment_to_addNoteFragment)
        }
    }

    private fun getDataFromFirestore() {
        firestore.collection("notes")
            .get().addOnSuccessListener { documents ->
                val notes = mutableListOf<Note>()
                for (document in documents) {
                    val note = document.toObject(Note::class.java)
                    note.id = document.id.toIntOrNull() ?: -1
                    notes.add(note)
                }
                noteAdapter.submitList(notes)
            }
            .addOnFailureListener { exception -> }
    }

    private fun deleteNoteFromFirestore(note: Note) {
        firestore.collection("notes")
            .document(note.id.toString()).delete()
            .addOnSuccessListener {
                getDataFromFirestore()
            }.addOnFailureListener { exception ->
            }
    }

    private fun updateNoteInFirestore(note: Note) {
        val noteMap = mapOf(
            "title" to note.title,
            "description" to note.description, "data" to note.data,
            "color" to note.color
        )
        firestore.collection("notes")
            .document(note.id.toString()).set(noteMap)
            .addOnSuccessListener {
                getDataFromFirestore()
            }.addOnFailureListener { exception ->
            }
    }

    override fun onLongClick(noteModel: Note) {
        val builder = AlertDialog.Builder(requireContext())
        with(builder) {
            setTitle("Удалить заметку?")
            setPositiveButton("Удалить") { _, _ ->
                deleteNoteFromFirestore(noteModel)
            }
            setNegativeButton("Отмена") { dialog, _ -> dialog.cancel() }
            show ()
        }
    }

    override fun onClick(noteModel: Note) {
        val action =
            ListNoteFragmentDirections.actionNoteListFragmentToAddNoteFragment(noteModel.id)
        findNavController().navigate(
            action
        )
    }
}