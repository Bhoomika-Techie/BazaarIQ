package com.example.bazaariq.ui.entry

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.bazaariq.databinding.ActivityQuickEntryBinding
import com.example.bazaariq.viewmodel.CustomerViewModel

class QuickEntryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuickEntryBinding
    private val viewModel: CustomerViewModel by viewModels()
    private var selectedCustomerId: Int = -1
    private var entryType: String = "UDARI"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuickEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        entryType = intent.getStringExtra("TYPE") ?: "UDARI"
        binding.tvEntryType.text = if (entryType == "UDARI") "Add Udari 💰" else "Add Payment ✅"

        // Load customers dropdown
        viewModel.allCustomers.observe(this) { customers ->
            val names = customers.map { it.name }
            val adapter = android.widget.ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                names
            )
            binding.spinnerCustomer.adapter = adapter

            binding.spinnerCustomer.onItemSelectedListener =
                object : android.widget.AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: android.widget.AdapterView<*>?,
                        view: android.view.View?,
                        position: Int,
                        id: Long
                    ) {
                        selectedCustomerId = customers[position].id
                    }
                    override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
                }
        }

        // Save button
        binding.btnSave.setOnClickListener {
            val amount = binding.etAmount.text.toString().toDoubleOrNull()
            val note = binding.etNote.text.toString()

            if (selectedCustomerId == -1) {
                Toast.makeText(this, "Select a customer!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (amount == null || amount <= 0) {
                Toast.makeText(this, "Enter valid amount!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (entryType == "UDARI") {
                viewModel.addUdari(selectedCustomerId, amount, note)
                Toast.makeText(this, "Udari Added! ₹$amount", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.addPayment(selectedCustomerId, amount, note)
                Toast.makeText(this, "Payment Added! ₹$amount", Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }
}