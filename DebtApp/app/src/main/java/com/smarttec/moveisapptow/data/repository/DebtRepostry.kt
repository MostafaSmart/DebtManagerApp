package com.smarttec.moveisapptow.data.repository

import android.content.ContentValues
import androidx.lifecycle.LiveData

import com.smarttec.moveisapptow.data.db.dao.DebtDao
import com.smarttec.moveisapptow.data.models.CustomerDebtsSummary

import com.smarttec.moveisapptow.data.models.Debt
import com.smarttec.moveisapptow.data.models.DebtWithCustomer


class DebtRepostry(val debtDao: DebtDao) {


    suspend fun insertDebt( debt: Debt):Long {
        return debtDao.insertDebt(debt)
//        Log.d(ContentValues.TAG, "done : ${response.code()}")
    }
    suspend fun updateDebt( debt: Debt) {
         debtDao.updateDebt(debt)
//        Log.d(ContentValues.TAG, "done : ${response.code()}")
    }


     fun getDebtWithCustmor(customer_id:Int):LiveData<List<DebtWithCustomer>> {
         return debtDao.getDebtsWithCustomer(customer_id)

     }




//    suspend fun getCustomerDebtsSummary(type: String): List<CustomerDebtsSummary> {
//       var list1 =  customerDao.getCustomerDebtsSummary("له")
//        var list2 =  customerDao.getCustomerDebtsSummary("عليه")
//
//        var list3 = list1 + list2
//        return  list3
//
//    }

//    suspend fun getAllCustmors(): List<Customer> {
//
//        return  customerDao.getAllCustomers()
//
//    }


}