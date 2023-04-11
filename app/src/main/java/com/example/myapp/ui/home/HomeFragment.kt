package com.example.myapp.ui.home

import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapp.R
import com.example.myapp.bpackage com.example.myapp.ui.home

import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapp.R
import com.example.myapp.base.BaseAdapterBinding
import com.example.myapp.databinding.FragmentHomeBinding
import com.example.myapp.databinding.ItemTaskBinding
import com.example.myapp.model.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment(),BaseAdapterBinding.BindAdapterListener {

    private var _binding: FragmentHomeBinding? = null
    private var taskAdapter:BaseAdapterBinding<Task>?=null
    private lateinit var database: DatabaseReference

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
//initialization of firebase
        FirebaseApp.initializeApp(requireContext())
        //get database instance of fire base
        database = FirebaseDatabase.getInstance().getReference("user")


//        set Recycler view
        setRecyclerView()

        //access data from fire base and set into adapter
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = mutableListOf<Task>()
                for (childSnapshot in snapshot.children) {
//                    val item = childSnapshot.getValue(Task::class.java)
                    val dataMap = childSnapshot.getValue() as Map<*, *>
                    val item = Task(
                        dataMap["strike"] as Boolean,
                        dataMap["title"] as String,
                    )
                    data.add(item)

                    Log.d("tag","Got value ${childSnapshot.children.forEach { 
                        it.value
                    }}")
                }
                // Step 5: Store the list of data retrieved from Firebase in an instance variable
                taskAdapter!!.setData(data)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error
            }
        })


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setRecyclerView() {
        taskAdapter = BaseAdapterBinding(
            requireActivity(),
            arrayListOf(),
            this,
            R.layout.item_task
        )
        binding.rcvTask.adapter =taskAdapter
    }

    //for data set in ui
    override fun onBind(holder: BaseAdapterBinding.DataBindingViewHolder, position: Int) {
        (holder.binding as ItemTaskBinding).apply {
            item = taskAdapter!!.getItem(position)

            cbTitle.paintFlags = cbTitle.paintFlags
            if (taskAdapter!!.getItem(position).strike){
                cbTitle.paintFlags = cbTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }else{
                cbTitle.paintFlags = cbTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
            cbTitle.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked){
                    buttonView.paintFlags =buttonView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }else{
                    buttonView.paintFlags =buttonView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                }
            }
        }
    }

}