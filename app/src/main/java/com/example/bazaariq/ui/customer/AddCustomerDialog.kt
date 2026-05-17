package com.example.bazaariq.ui.customer

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.bazaariq.R

class AddCustomerDialog(
    context: Context,
    private val onCustomerAdded: (name: String, phone: String) -> Unit
) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_add_customer)

        val etName = findViewById<EditText>(R.id.etCustomerName)
        val etPhone = findViewById<EditText>(R.id.etCustomerPhone)
        val btnAdd = findViewById<Button>(R.id.btnAddCustomer)

        btnAdd.setOnClickListener {
            val name = etName.text.toString()
            val phone = etPhone.text.toString()
            if (name.isNotEmpty()) {
                onCustomerAdded(name, phone)
                dismiss()
            }
        }
    }
}