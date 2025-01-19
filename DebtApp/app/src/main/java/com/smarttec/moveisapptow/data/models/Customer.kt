package com.smarttec.moveisapptow.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customer")
data class Customer(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // المفتاح الأساسي
     val name: String, // اسم العميل
   val phone: String, // رقم الهاتف
   val type: String, // نوع العميل
    val permissible_limit: Int? // الحد المسموح به
)



