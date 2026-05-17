package com.example.bazaariq.ui.customer

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.bazaariq.databinding.ActivityCustomerLedgerBinding
import com.example.bazaariq.viewmodel.CustomerViewModel

class CustomerLedgerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomerLedgerBinding
    private val viewModel: CustomerViewModel by viewModels()
    private var customerId: Int = -1
    private var customerName: String = ""
    private var customerPhone: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerLedgerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        customerId = intent.getIntExtra("CUSTOMER_ID", -1)
        customerName = intent.getStringExtra("CUSTOMER_NAME") ?: ""

        binding.tvCustomerName.text = customerName

        // Observe transactions
        viewModel.getTransactionsForCustomer(customerId).observe(this) { transactions ->
            var totalUdari = 0.0
            var totalPaid = 0.0
            val sb = StringBuilder()

            transactions.forEach { t ->
                if (t.type == "UDARI") {
                    totalUdari += t.amount
                    sb.append("💰 Udari: ₹${t.amount}")
                } else {
                    totalPaid += t.amount
                    sb.append("✅ Payment: ₹${t.amount}")
                }
                if (t.note.isNotEmpty()) sb.append(" (${t.note})")
                sb.append("\n")
            }

            val pending = totalUdari - totalPaid
            binding.tvTotalUdari.text = "Total Udari: ₹$totalUdari"
            binding.tvTotalPaid.text = "Total Paid: ₹$totalPaid"
            binding.tvPendingAmount.text = "Pending: ₹$pending"
            binding.tvTransactions.text = if (sb.isEmpty()) "No transactions yet" else sb.toString()

            if (pending > 0) {
                binding.tvPendingAmount.setTextColor(android.graphics.Color.RED)
            } else {
                binding.tvPendingAmount.setTextColor(android.graphics.Color.GREEN)
            }
        }

        // WhatsApp reminder
        binding.btnWhatsapp.setOnClickListener {
            viewModel.allCustomers.observe(this) { customers ->
                val customer = customers.find { it.id == customerId }
                customer?.let {
                    customerPhone = it.phoneNumber
                    val pending = it.totalPending
                    val message = "Namaste $customerName ji! Aapka BazaarIQ mein ₹$pending baaki hai. Kripa payment karein. Shukriya!"
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse("https://wa.me/91$customerPhone?text=${Uri.encode(message)}")
                    }
                    startActivity(intent)
                }
            }
        }
    }
}