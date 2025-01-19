package com.smarttec.moveisapptow.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "debts",
    foreignKeys = [ForeignKey(
        entity = Customer::class,
        parentColumns = ["id"],
        childColumns = ["customer_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Debt(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // المفتاح الأساسي
   val description: String, // وصف الدين
   val amount: Double, // المبلغ
     val date: String, // تاريخ الدين
    val d_type: String, // نوع الدين
  val customer_id: Int // المفتاح الأجنبي
)
