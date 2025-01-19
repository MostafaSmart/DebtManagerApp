package com.smarttec.moveisapptow.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.smarttec.moveisapptow.data.models.Debt
import com.smarttec.moveisapptow.data.models.DebtWithCustomer

@Dao
interface DebtDao {
    @Insert
    suspend fun insertDebt(debt: Debt): Long


    @Update
    suspend fun updateDebt(debt: Debt)

    @Query("SELECT * FROM debts WHERE customer_id = :customerId")
    suspend fun getDebtsByCustomerId(customerId: Int): List<Debt>

    @Query("SELECT * FROM debts WHERE id = :id")
    suspend fun getDebtById(id: Int): Debt?

    @Delete
    suspend fun deleteDebt(debt: Debt)

    @Query("""
        SELECT 
            d.*, 
            c.id AS customer_id,
            c.name AS customer_name, 
            c.phone, 
            c.type, 
            c.permissible_limit 
        FROM 
            debts d 
        JOIN 
            customer c ON d.customer_id = c.id
        WHERE 
            c.id = :customerId
    """)
     fun getDebtsWithCustomer(customerId: Int): LiveData<List<DebtWithCustomer>>


}
