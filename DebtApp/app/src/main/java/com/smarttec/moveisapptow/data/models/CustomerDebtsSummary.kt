package com.smarttec.moveisapptow.data.models

data class CustomerDebtsSummary(
    val customer_id: Int,
    val customer_name: String,
    val total_debts_on: Double,
    val total_debts_for: Double,
    val count_debts_on: Int,
    val count_debts_for: Int
)

