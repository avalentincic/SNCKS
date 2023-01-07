package com.example.sncksapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lib.Item
import com.example.lib.VendingMachine
import com.example.sncksapp.databinding.ActivityMainBinding.inflate
import com.example.sncksapp.databinding.FragmentDetailBinding
import com.example.sncksapp.databinding.FragmentListBinding
import com.squareup.picasso.Picasso

class DetailFragment : Fragment() {
    var id:String? = null
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var app: MyApplication
    lateinit var items: ArrayList<Item>
    lateinit var vending: VendingMachine
    lateinit var adapter: ItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MyApplication
        arguments?.let {
            id = it.getString("ID")
        }
        vending = app.findVendingById(id ?: "null")
        items = vending.items
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        val rvItems = binding.rvVendingItems
        adapter = ItemsAdapter(items)


        rvItems.adapter = adapter
        rvItems.layoutManager = LinearLayoutManager(activity?.baseContext)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = vending.name
    }

}

class ItemsAdapter(private val mItems: ArrayList<Item>) : RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {

    /*
    interface MyOnClick {
        fun onItemClick(itemView: View?, position: Int)
        fun onItemLongClick(itemView: View?, position: Int)
    }

    private lateinit var listener: MyOnClick

    fun setOnItemClickListener(listener: MyOnClick) {
        this.listener = listener
    }*/

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewIcon: ImageView = itemView.findViewById(R.id.ivItemIcon)
        val nameTextView: TextView = itemView.findViewById(R.id.tvItemName)
        val qtyTextView: TextView = itemView.findViewById(R.id.tvQuantity)
        val priceTextView: TextView = itemView.findViewById(R.id.tvPrice)
        val reminderBtn: Button = itemView.findViewById(R.id.btnReminder)

        /*
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
        }*/
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val view = inflater.inflate(R.layout.item_vending_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: Item = mItems[position]

        val tvName = holder.nameTextView
        tvName.text = item.name

        val tvQty = holder.qtyTextView
        tvQty.text = item.quantity.toString()

        val tvPrice = holder.priceTextView
        tvPrice.text = item.price.toString()

        if (item.quantity!! > 0){
            holder.reminderBtn.visibility = View.INVISIBLE
        }

        Picasso.get().load(R.drawable.snack)
            .placeholder(R.drawable.snack)
            .fit()
            .into(holder.imageViewIcon)
    }

    override fun getItemCount(): Int {
        return mItems.size
    }
}