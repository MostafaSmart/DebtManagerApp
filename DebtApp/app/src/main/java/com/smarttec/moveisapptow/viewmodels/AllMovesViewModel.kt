package com.smarttec.moveisapptow.viewmodels

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smarttec.moveisapptow.data.API_KEY
import com.smarttec.moveisapptow.data.CONTENT_TYPE
import com.smarttec.moveisapptow.data.models.MovesModel
import com.smarttec.moveisapptow.data.models.MovesResponse
import com.smarttec.moveisapptow.data.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllMovesViewModel: ViewModel(){

    private var moveis: MutableLiveData<List<MovesModel>> = MutableLiveData<List<MovesModel>>()


    init {
        getMoves()
    }

    private fun getMoves() {
        RetrofitInstance.movesApi.getAllMoves(CONTENT_TYPE, API_KEY).enqueue(object :
            Callback<MovesResponse> {
            override fun onResponse(call: Call<MovesResponse>, response: Response<MovesResponse>) {
                if (response.isSuccessful) {
                    Log.d(ContentValues.TAG, "resposn  : ${response.body()}")
                    moveis.value = response.body()?.results ?: emptyList()
                } else {
                    Log.d(ContentValues.TAG, "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<MovesResponse>, t: Throwable) {
                Log.d(ContentValues.TAG, t.message.toString())
            }
        })
    }



    fun observeCategories(): LiveData<List<MovesModel>> {
        return moveis
    }


}