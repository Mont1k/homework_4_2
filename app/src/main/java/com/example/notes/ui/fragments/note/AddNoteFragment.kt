package com.example.notes.ui.fragments.note

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.notes.R
import com.example.notes.data.model.Note
import com.example.notes.databinding.FragmentNoteAddBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date

class AddNoteFragment : Fragment() {

    private lateinit var binding: FragmentNoteAddBinding
    private val firestore = FirebaseFirestore.getInstance()
    private var noteId: Int? = null
    private var color: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteAddBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvData.text = getCurrentTime()
        updateNote()
        setupListener()
    }

    private fun updateNote() {
        arguments?.let { args ->
            noteId = args.getInt("noteId", -1)
        }
        if (noteId != -1) {
            val noteRef = firestore.collection("notes")
                .document(noteId.toString())
            noteRef.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    val note = document.toObject(Note::class.java)
                    note?.let { item ->
                        binding.etTitle.setText(item.title)
                        binding.etDescription.setText(item.description)
                        binding.tvData.text = item.data
                    }
                }
            }
        }
    }

    private fun setupListener() = with(binding) {
        triTochki.setOnClickListener {
            showColorDialog()
        }
        imgBack.setOnClickListener {
            findNavController().navigateUp()
        }
        tvGotovo.setOnClickListener {
            val title = etTitle.text.toString()
            val text = etDescription.text.toString()
            val data = tvData.text.toString()
            if (noteId != -1) {
                val updateNote = Note(title, text, data, color.hashCode())
                updateNote.id = noteId!!
                firestore.collection("notes").document(noteId.toString())
                    .set(updateNote).addOnSuccessListener {
                        findNavController().navigateUp()
                    }
            } else {
                val newNote = Note(title, text, data, color.hashCode())
                firestore.collection("notes").add(newNote).addOnSuccessListener {
                    findNavController().navigateUp()
                }
            }
        }
    }

    private fun showColorDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.color_picker, null)
        builder.setView(dialogView)
        val dialog = builder.create()
        val colorMap = mapOf(
            R.id.color_yellow2 to R.color.yellow2,
            R.id.color_purple2 to R.color.purple2,
            R.id.color_pink2 to R.color.pink2,
            R.id.color_red2 to R.color.red,
            R.id.color_green2 to R.color.green2,
            R.id.color_blue2 to R.color.blue2
        )
        colorMap.forEach { (viewId, colorId) ->
            dialogView.findViewById<View>(viewId).setOnClickListener {
                color = ContextCompat.getColor(requireContext(), colorId)
                dialog.dismiss()
            }
        }
        dialog.show()
        val window = dialog.window
        val layoutParams = window?.attributes
        layoutParams?.gravity = Gravity.END or Gravity.TOP
        layoutParams?.x = 100
        layoutParams?.y = 100
        window?.attributes = layoutParams
    }

    @SuppressLint("SimpleDateFormat")
    private fun getCurrentTime(): String {
        val date = SimpleDateFormat("dd MMMM HH:mm")
        return date.format(Date())
    }
}