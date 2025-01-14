package com.example.notes.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.notes.R
import com.example.notes.data.Note
import com.example.notes.databinding.FragmentNoteAddBinding
import java.text.SimpleDateFormat
import java.util.Locale

class AddNoteFragment : Fragment() {
    private var selectedColor: Int = 0xFFFFFF
    private var _binding: FragmentNoteAddBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Обработка выбора цвета с использованием ViewBinding
        binding.colorRed.setOnClickListener {
            selectedColor = binding.colorRed.context.getColor(R.color.red)
        }
        binding.colorGreen.setOnClickListener {
            selectedColor = binding.colorGreen.context.getColor(R.color.green)
        }
        binding.colorBlue.setOnClickListener {
            selectedColor = binding.colorBlue.context.getColor(R.color.blue)
        }

        binding.buttonSaveNote.setOnClickListener {
            val title = binding.editTextTitle.text.toString().trim()
            val description = binding.editTextDescription.text.toString().trim()

            if (title.isNotBlank() && description.isNotBlank()) {
                val currentDate = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault()).format(System.currentTimeMillis())

                val note = Note(title, description, selectedColor, currentDate)

                parentFragmentManager.setFragmentResult("add_note", Bundle().apply {
                    putParcelable("note", note)
                })

                findNavController().navigateUp()
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
