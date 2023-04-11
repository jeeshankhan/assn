package com.example.myapp.ui.gallery

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Viewpackage com.example.myapp.ui.gallery

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.myapp.databinding.FragmentGalleryBinding
import com.example.myapp.model.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
            ViewModelProvider(this)[GalleryViewModel::class.java]

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        database = FirebaseDatabase.getInstance().reference
//        val textView: TextView = binding.textGallery
//        galleryViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }

        binding.btnAdd.setOnClickListener {
            uploadUserData(binding.edtTitle.text.toString(),false)
        }

        return binding.root
    }

    private fun uploadUserData(name: String, isStrike: Boolean=false) {
        val imageRef = database.child("user")
        val task = Task(isStrike,name)
        imageRef.child(name).setValue(task).addOnSuccessListener {
       Toast.makeText(context,"Success",Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }.addOnFailureListener {
            Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}