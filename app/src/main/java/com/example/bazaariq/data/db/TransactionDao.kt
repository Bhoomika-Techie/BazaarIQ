package com.example.bazaariq.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.bazaariq.data.model.Transaction

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions WHERE customerId = :customerId ORDER BY date DESC")
    fun getTransactionsForCustomer(customerId: Int): LiveData<List<Transaction>>

    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactions(): LiveData<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE date >= :startOfDay ORDER BY date DESC")
    fun getTodayTransactions(startOfDay: Long): LiveData<List<Transaction>>

    @Insert
    suspend fun insertTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    @Query("SELECT SUM(amount) FROM transactions WHERE customerId = :customerId AND type = 'UDARI'")
    suspend fun getTotalUdari(customerId: Int): Double?

    @Query("SELECT SUM(amount) FROM transactions WHERE customerId = :customerId AND type = 'PAYMENT'")
    suspend fun getTotalPayment(customerId: Int): Double?

    @Query("SELECT SUM(amount) FROM transactions WHERE date >= :startOfDay AND type = 'UDARI'")
    fun getTodayTotalUdari(startOfDay: Long): LiveData<Double?>

    @Query("SELECT SUM(amount) FROM transactions WHERE date >= :startOfDay AND type = 'PAYMENT'")
    fun getTodayTotalPayment(startOfDay: Long): LiveData<Double?>
}