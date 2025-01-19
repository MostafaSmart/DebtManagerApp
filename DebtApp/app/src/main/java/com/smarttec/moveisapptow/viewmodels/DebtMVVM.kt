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
import com.smarttec.moveisapptow.data.models.Debt
import com.smarttec.moveisapptow.data.models.DebtWithCustomer
import com.smarttec.moveisapptow.data.repository.CustomerRepostry
import com.smarttec.moveisapptow.data.repository.DebtRepostry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext



class DebtMVVM(application: Application) : AndroidViewModel(application) {


    private val _insertResult = MutableLiveData<Result<Long>>()
    val insertResult: LiveData<Result<Long>> get() = _insertResult


    private val _updateResult = MutableLiveData<Result<String>>()
    val updateResult: LiveData<Result<String>> get() = _updateResult


    private  var debtRepostry: DebtRepostry
    private lateinit var  debtWithCustmor :LiveData<List<DebtWithCustomer>>



    init {
        val mealDao = AppDatabase.getInstance(application).debtDao()
        debtRepostry = DebtRepostry(mealDao)

    }


    fun insertDebt(debt: Debt) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = debtRepostry.insertDebt(debt)
                _insertResult.postValue(Result.success(result))
            } catch (e: Exception) {
                _insertResult.postValue(Result.failure(e))
            }

        }
    }



    fun updateDebt(debt: Debt) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                 debtRepostry.updateDebt(debt)
                _updateResult.postValue(Result.success("تمت العملية بنجاح"))
            } catch (e: Exception) {
                _updateResult.postValue(Result.success("فشلت العملية!"))
            }

        }
    }


    fun getDebtWithCustmor(cust_id:Int) {
        debtWithCustmor =  debtRepostry.getDebtWithCustmor(cust_id)

    }


    fun observeDebtWithCostmr(): LiveData<List<DebtWithCustomer>> {
        return debtWithCustmor
    }

//    fun observeAllCousmers(): LiveData<List<Customer>> {
//        return allCustomer
//    }
}