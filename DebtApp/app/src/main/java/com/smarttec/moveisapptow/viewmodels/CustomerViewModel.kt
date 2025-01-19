package com.smarttec.moveisapptow.viewmodels

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smarttec.moveisapptow.data.db.AppDatabase
import com.smarttec.moveisapptow.data.models.Customer
import com.smarttec.moveisapptow.data.models.CustomerDebtsSummary
import com.smarttec.moveisapptow.data.repository.CustomerRepostry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class CustomerViewModel(application: Application) : AndroidViewModel(application) {

    private  var customerRepository: CustomerRepostry
     private lateinit var customerDebtsSummary1: LiveData<List<CustomerDebtsSummary>>
    private val _insertResult = MutableLiveData<Result<Long>>()
    val insertResult: LiveData<Result<Long>> get() = _insertResult
      var allCustomer: LiveData<List<Customer>>



    init {
        val mealDao = AppDatabase.getInstance(application).customerDao()
        customerRepository = CustomerRepostry(mealDao)

        allCustomer = customerRepository.allCustmoerList

    }


    fun insertCustomer(customer: Customer) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = customerRepository.insertFavoriteMeal(customer)
                _insertResult.postValue(Result.success(result))
            } catch (e: Exception) {
                _insertResult.postValue(Result.failure(e))
            }
        }
    }

    fun getCutmerDebtSummry(type:String){
        customerDebtsSummary1 = customerRepository.getCusmertDebtSymmry(type)
    }


    fun observeSummry(): LiveData<List<CustomerDebtsSummary>> {
        return customerDebtsSummary1
    }

    fun observeAllCousmers(): LiveData<List<Customer>> {
        return allCustomer
    }
}

