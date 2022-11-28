package com.example.sncksapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lib.VendingMachine
import com.example.sncksapp.databinding.FragmentListBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private lateinit var app: MyApplication
    lateinit var vms: ArrayList<VendingMachine>
    lateinit var adapter: GoodsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        app = activity?.application as MyApplication

        var rvItems = binding.rvItems
        vms = app.vendingMachines
        adapter = GoodsAdapter(vms)


        rvItems.adapter = adapter
        rvItems.layoutManager = LinearLayoutManager(activity?.baseContext)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Vending Machines"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class GoodsAdapter(private val vendingMachines: ArrayList<VendingMachine>) : RecyclerView.Adapter<GoodsAdapter.ViewHolder>() {

    interface MyOnClick {
        fun onItemClick(itemView: View?, position: Int)
        fun onItemLongClick(itemView: View?, position: Int)
    }

    private lateinit var listener: MyOnClick

    fun setOnItemClickListener(listener: MyOnClick) {
        this.listener = listener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewIcon: ImageView = itemView.findViewById(R.id.imageViewIcon)
        val nameTextView: TextView = itemView.findViewById(R.id.tvGood_name)
        val qtyTextView: TextView = itemView.findViewById(R.id.tvQuantity)
        val priceTextView: TextView = itemView.findViewById(R.id.tvPrice)

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

        val tvQty = holder.qtyTextView
        tvQty.text = vendingM.latitude.toString()

        val tvPrice = holder.priceTextView
        tvPrice.text = vendingM.longitude.toString()

        /*Picasso.get().load(R.drawable.hand_truck)
            .placeholder(R.drawable.hand_truck)
            .fit()
            .into(holder.imageViewIcon)*/
    }

    override fun getItemCount(): Int {
        return vendingMachines.size
    }
}