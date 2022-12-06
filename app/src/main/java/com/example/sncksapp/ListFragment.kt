package com.example.sncksapp

import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lib.VendingMachine
import com.example.sncksapp.databinding.FragmentListBinding
import com.squareup.picasso.Picasso

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private lateinit var app: MyApplication
    lateinit var vms: ArrayList<VendingMachine>
    lateinit var adapter: MachinesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        app = activity?.application as MyApplication

        val rvItems = binding.rvItems
        vms = app.vendingMachines
        adapter = MachinesAdapter(vms)

        adapter.setOnItemClickListener(object: MachinesAdapter.MyOnClick{

            override fun onItemClick(itemView: View?, position: Int) {
                val m1 = vms[position]
                val bundle = bundleOf(
                    "ID" to m1.id.toString()
                )
                findNavController().navigate(
                    R.id.action_ListFragment_to_detailFragment, bundle
                )
            }

            override fun onItemLongClick(itemView: View?, position: Int) {
                val m1 = vms[position]
                val bundle = bundleOf(
                    "ID" to m1.id.toString()
                )
                findNavController().navigate(
                    R.id.action_ListFragment_to_editVendingFragment, bundle
                )
            }

            override fun onItemDrag(itemView: View?, position: Int) {
                val builder = AlertDialog.Builder(activity as AppCompatActivity)
                builder.setTitle("Delete")
                builder.setMessage(vms[position].name)
                builder.setIcon(android.R.drawable.ic_dialog_alert)
                builder.setPositiveButton("Yes"){dialogInterface, which ->
                    vms.removeAt(position)
                    adapter.notifyDataSetChanged()
                    app.saveToFile()
                }
                builder.setNegativeButton("No"){dialogInterface, which ->
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        })


        rvItems.adapter = adapter
        rvItems.layoutManager = LinearLayoutManager(activity?.baseContext)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //(activity as AppCompatActivity).supportActionBar?.title = "Vending Machines"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class MachinesAdapter(private val vendingMachines: ArrayList<VendingMachine>) : RecyclerView.Adapter<MachinesAdapter.ViewHolder>() {

    interface MyOnClick {
        fun onItemClick(itemView: View?, position: Int)
        fun onItemLongClick(itemView: View?, position: Int)
        fun onItemDrag(itemView: View?, position: Int)
    }

    private lateinit var listener: MyOnClick

    fun setOnItemClickListener(listener: MyOnClick) {
        this.listener = listener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewIcon: ImageView = itemView.findViewById(R.id.imageViewIcon)
        val nameTextView: TextView = itemView.findViewById(R.id.tvMachine_name)

        init {

            itemView.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemLongClick(itemView, position)
                }
                return@setOnLongClickListener true
            }

            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(itemView, position)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val view = inflater.inflate(R.layout.item_vending, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val vendingM: VendingMachine = vendingMachines[position]
        // Set item views based on your views and data model
        val tvName = holder.nameTextView
        tvName.text = vendingM.name

        Picasso.get().load(R.drawable.vending_machine)
            .placeholder(R.drawable.vending_machine)
            .fit()
            .into(holder.imageViewIcon)
    }

    override fun getItemCount(): Int {
        return vendingMachines.size
    }
}