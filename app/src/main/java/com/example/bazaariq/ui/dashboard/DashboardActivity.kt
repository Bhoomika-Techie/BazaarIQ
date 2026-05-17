package com.example.bazaariq.ui.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.bazaariq.databinding.ActivityDashboardBinding
import com.example.bazaariq.ui.customer.CustomerListActivity
import com.example.bazaariq.ui.entry.QuickEntryActivity
import com.example.bazaariq.viewmodel.CustomerViewModel

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private val viewModel: CustomerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Observe today's summary
        viewModel.getTodayTotalUdari().observe(this) { udari ->
            val amount = udari ?: 0.0
            binding.tvTodaySales.text = "Today Sales: ₹$amount"
        }

        viewModel.getTodayTotalPayment().observe(this) { payment ->
            val amount = payment ?: 0.0
            binding.tvTodayPayment.text = "Today Received: ₹$amount"
        }

        // Button clicks
        binding.btnAddUdari.setOnClickListener {
            startActivity(Intent(this, QuickEntryActivity::class.java).apply {
                putExtra("TYPE", "UDARI")
            })
        }

        binding.btnAddPayment.setOnClickListener {
            startActivity(Intent(this, QuickEntryActivity::class.java).apply {
                putExtra("TYPE", "PAYMENT")
            })
        }

        binding.btnCustomers.setOnClickListener {
            startActivity(Intent(this, CustomerListActivity::class.java))
        }
    }
}