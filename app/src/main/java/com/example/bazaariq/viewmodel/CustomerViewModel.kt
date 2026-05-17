package com.example.bazaariq.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.bazaariq.data.db.AppDatabase
import com.example.bazaariq.data.model.Customer
import com.example.bazaariq.data.model.Transaction
import com.example.bazaariq.data.repository.BazaarRepository
import kotlinx.coroutines.launch
import java.util.Calendar

class CustomerViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BazaarRepository
    val allCustomers: LiveData<List<Customer>>
    val customersWithDues: LiveData<List<Customer>>

    init {
        val db = AppDatabase.getDatabase(application)
        repository = BazaarRepository(db)
        allCustomers = repository.allCustomers
        customersWithDues = repository.customersWithDues
    }

    // Add new customer
    fun addCustomer(name: String, phone: String) = viewModelScope.launch {
        val customer = Customer(
            name = name,
            phoneNumber = phone
        )
        repository.insertCustomer(customer)
    }

    // Delete customer
    fun deleteCustomer(customer: Customer) = viewModelScope.launch {
        repository.deleteCustomer(customer)
    }

    // Add Udari (Credit given to customer)
    fun addUdari(customerId: Int, amount: Double, note: String = "") =
        viewModelScope.launch {
            val transaction = Transaction(
                customerId = customerId,
                amount = amount,
                type = "UDARI",
                note = note
            )
            repository.insertTransaction(transaction)
            repository.updateCustomerBalance(customerId)
        }

    // Add Payment (Customer pays back)
    fun addPayment(customerId: Int, amount: Double, note: String = "") =
        viewModelScope.launch {
            val transaction = Transaction(
                customerId = customerId,
                amount = amount,
                type = "PAYMENT",
                note = note
            )
            repository.insertTransaction(transaction)
            repository.updateCustomerBalance(customerId)
        }

    // Get transactions for a customer
    fun getTransactionsForCustomer(customerId: Int) =
        repository.getTransactionsForCustomer(customerId)

    // Today's summary
    fun getTodayStartTime(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        return calendar.timeInMillis
    }

    fun getTodayTransactions() =
        repository.getTodayTransactions(getTodayStartTime())

    fun getTodayTotalUdari() =
        repository.getTodayTotalUdari(getTodayStartTime())

    fun getTodayTotalPayment() =
        repository.getTodayTotalPayment(getTodayStartTime())
}