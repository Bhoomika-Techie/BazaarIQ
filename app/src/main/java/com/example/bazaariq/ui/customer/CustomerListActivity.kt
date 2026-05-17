package com.example.bazaariq.ui.customer

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bazaariq.databinding.ActivityCustomerListBinding
import com.example.bazaariq.viewmodel.CustomerViewModel

class CustomerListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomerListBinding
    private val viewModel: CustomerViewModel by viewModels()
    private lateinit var adapter: CustomerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup RecyclerView
        adapter = CustomerAdapter(emptyList()) { customer ->
            // Click on customer → go to ledger
            val intent = Intent(this, CustomerLedgerActivity::class.java)
            intent.putExtra("CUSTOMER_ID", customer.id)
            intent.putExtra("CUSTOMER_NAME", customer.name)
            startActivity(intent)
        }
        binding.rvCustomers.layoutManager = LinearLayoutManager(this)
        binding.rvCustomers.adapter = adapter

        // Observe customers
        viewModel.allCustomers.observe(this) { customers ->
            adapter.updateList(customers)
            binding.tvCustomerCount.text = "Total Customers: ${customers.size}"
        }

        // Add customer button
        binding.btnAddCustomer.setOnClickListener {
            AddCustomerDialog(this) { name, phone ->
                viewModel.addCustomer(name, phone)
            }.show()
        }
    }
}