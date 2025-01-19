package com.smarttec.moveisapptow.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.smarttec.moveisapptow.data.models.Customer
import com.smarttec.moveisapptow.data.models.CustomerDebtsSummary

@Dao
interface CustomerDao {
    @Insert
    suspend fun insertCustomer(customer: Customer): Long

    @Query("SELECT * FROM customer WHERE id = :id")
    suspend fun getCustomerById(id: Int): Customer?

    @Query("SELECT * FROM customer")
    fun getAllCustomers() : LiveData<List<Customer>>

    @Delete
    suspend fun deleteCustomer(customer: Customer)


    @Query("""
        SELECT 
            customer.id AS customer_id,
            customer.name AS customer_name,
            SUM(CASE WHEN debts.d_type= 'عليه' THEN debts.amount ELSE 0 END) AS total_debts_on,
            SUM(CASE WHEN debts.d_type = 'له' THEN debts.amount ELSE 0 END) AS total_debts_for,
            COUNT(CASE WHEN debts.d_type = 'عليه' THEN 1 ELSE NULL END) AS count_debts_on,
            COUNT(CASE WHEN debts.d_type = 'له' THEN 1 ELSE NULL END) AS count_debts_for
        FROM 
            customer
        LEFT JOIN 
            debts 
        ON 
            customer.id = debts.customer_id
        WHERE 
            customer.type = :customerType
        GROUP BY 
            customer.id, customer.name
    """)
    fun getCustomerDebtsSummary(customerType: String):LiveData<List<CustomerDebtsSummary>>



    

}
