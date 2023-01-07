package com.example.sncksapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lib.VendingMachine
import com.example.sncksapp.databinding.FragmentEditVendingBinding

class EditVendingFragment : Fragment() {

    private var _binding: FragmentEditVendingBinding? = null
    private val binding get() = _binding!!

    private lateinit var app: MyApplication
    private var id: String? = null
    private var vending: VendingMachine? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MyApplication
        arguments?.let {
            id = it.getString("ID")
        }
        if(id != null) vending = app.findVendingById(id ?: "null")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentEditVendingBinding.inflate(inflater, container, false)

        if(vending != null) {
            binding.plainTextInput1.setText(vending?.name)
            binding.plainTextInput2.setText(vending?.latitude.toString())
            binding.plainTextInput3.setText(vending?.longitude.toString())
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var str = "Add Vending Machine"
        if (vending != null) str = "Edit ${vending?.name}"
        (activity as AppCompatActivity).supportActionBar?.title = str

        var name = binding.plainTextInput1.text
        var latitude = binding.plainTextInput2.text
        var longitude = binding.plainTextInput3.text

        binding.buttonAdd.setOnClickListener{
            save(id, name.toString(), latitude.toString().toDouble(), longitude.toString().toDouble())
            name.clear()
            latitude.clear()
            longitude.clear()
            findNavController().navigate(
                R.id.action_editVendingFragment_to_ListFragment
            )
        }
    }

    private fun save(id: String?, name: String, lat:Double, long:Double){
        if (vending != null){
            app.updateVending(id!!, name , lat, long)
        } else {
            app.vendingMachines.add(VendingMachine(name, lat, long))
        }
        app.saveToFile()
        app.saveToDB()
    }

}
