package com.smarttec.moveisapptow.data.models

data class DebtWithCustomer(
    val id: Int, // من جدول debts
    val description: String,
    val amount: Double,
    val date: String,
    val d_type: String,
    val customer_id: Int,
    val customer_name: String, // من جدول customer
    val phone: String,
    val type: String,
    val permissible_limit: Int
)


