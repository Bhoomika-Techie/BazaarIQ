package com.example.bazaariq.data.repository

import androidx.lifecycle.LiveData
import com.example.bazaariq.data.db.AppDatabase
import com.example.bazaariq.data.model.Customer
import com.example.bazaariq.data.model.Transaction

class BazaarRepository(private val db: AppDatabase) {

    // Customer operations
    val allCustomers: LiveData<List<Customer>> = db.customerDao().getAllCustomers()
    val customersWithDues: LiveData<List<Customer>> = db.customerDao().getCustomersWithDues()

    suspend fun insertCustomer(customer: Customer) {
        db.customerDao().insertCustomer(customer)
    }

    suspend fun updateCustomer(customer: Customer) {
        db.customerDao().updateCustomer(customer)
    }

    suspend fun deleteCustomer(customer: Customer) {
        db.customerDao().deleteCustomer(customer)
    }

    suspend fun getCustomerById(id: Int): Customer {
        return db.customerDao().getCustomerById(id)
    }

    // Transaction operations
    val allTransactions: LiveData<List<Transaction>> = db.transactionDao().getAllTransactions()

    fun getTransactionsForCustomer(customerId: Int): LiveData<List<Transaction>> {
        return db.transactionDao().getTransactionsForCustomer(customerId)
    }

    fun getTodayTransactions(startOfDay: Long): LiveData<List<Transaction>> {
        return db.transactionDao().getTodayTransactions(startOfDay)
    }

    suspend fun insertTransaction(transaction: Transaction) {
        db.transactionDao().insertTransaction(transaction)
    }

    // Balance calculations
    suspend fun updateCustomerBalance(customerId: Int) {
        val totalUdari = db.transactionDao().getTotalUdari(customerId) ?: 0.0
        val totalPaid = db.transactionDao().getTotalPayment(customerId) ?: 0.0
        val pending = totalUdari - totalPaid
        val customer = db.customerDao().getCustomerById(customerId)
        db.customerDao().updateCustomer(
            customer.copy(
                totalPaid = totalPaid,
                totalPending = pending
            )
        )
    }

    // Today summary
    fun getTodayTotalUdari(startOfDay: Long) =
        db.transactionDao().getTodayTotalUdari(startOfDay)

    fun getTodayTotalPayment(startOfDay: Long) =
        db.transactionDao().getTodayTotalPayment(startOfDay)
}