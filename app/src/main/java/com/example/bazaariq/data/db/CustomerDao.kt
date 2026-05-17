package com.example.bazaariq.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.bazaariq.data.model.Customer

@Dao
interface CustomerDao {

    @Query("SELECT * FROM customers ORDER BY name ASC")
    fun getAllCustomers(): LiveData<List<Customer>>

    @Query("SELECT * FROM customers WHERE id = :id")
    suspend fun getCustomerById(id: Int): Customer

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customer: Customer)

    @Update
    suspend fun updateCustomer(customer: Customer)

    @Delete
    suspend fun deleteCustomer(customer: Customer)

    @Query("SELECT * FROM customers WHERE totalPending > 0 ORDER BY totalPending DESC")
    fun getCustomersWithDues(): LiveData<List<Customer>>
}