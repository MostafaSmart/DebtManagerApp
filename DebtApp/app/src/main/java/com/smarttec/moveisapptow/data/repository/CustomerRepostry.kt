package com.smarttec.moveisapptow.data.repository

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.smarttec.moveisapptow.data.db.AppDatabase
import com.smarttec.moveisapptow.data.db.dao.CustomerDao
import com.smarttec.moveisapptow.data.models.Customer
import com.smarttec.moveisapptow.data.models.CustomerDebtsSummary
import com.smarttec.moveisapptow.data.models.DebtWithCustomer

class CustomerRepostry(val customerDao: CustomerDao) {

     val customerDebtsSummary1 :LiveData<List<CustomerDebtsSummary>> =customerDao.getCustomerDebtsSummary("عملاء")


    val allCustmoerList: LiveData<List<Customer>> = customerDao.getAllCustomers()

    suspend fun insertFavoriteMeal( customer: Customer):Long {
         return customerDao.insertCustomer(customer)
//        Log.d(ContentValues.TAG, "done : ${response.code()}")
    }

    fun getCusmertDebtSymmry(type:String):LiveData<List<CustomerDebtsSummary>> {
        return customerDao.getCustomerDebtsSummary(type)

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