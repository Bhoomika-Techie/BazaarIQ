package com.example.bazaariq.ui.customer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bazaariq.R
import com.example.bazaariq.data.model.Customer

class CustomerAdapter(
    private var customers: List<Customer>,
    private val onCustomerClick: (Customer) -> Unit
) : RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder>() {

    class CustomerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvCustomerName)
        val tvPhone: TextView = itemView.findViewById(R.id.tvCustomerPhone)
        val tvPending: TextView = itemView.findViewById(R.id.tvPendingAmount)
        val tvPaid: TextView = itemView.findViewById(R.id.tvPaidAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_customer, parent, false)
        return CustomerViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        val customer = customers[position]
        holder.tvName.text = customer.name
        holder.tvPhone.text = customer.phoneNumber
        holder.tvPending.text = "Pending: ₹${customer.totalPending}"
        holder.tvPaid.text = "Paid: ₹${customer.totalPaid}"

        // Color based on pending amount
        if (customer.totalPending > 0) {
            holder.tvPending.setTextColor(android.graphics.Color.RED)
        } else {
            holder.tvPending.setTextColor(android.graphics.Color.GREEN)
        }

        holder.itemView.setOnClickListener {
            onCustomerClick(customer)
        }
    }

    override fun getItemCount() = customers.size

    fun updateList(newList: List<Customer>) {
        customers = newList
        notifyDataSetChanged()
    }
}