package com.elsawy.expirydatetracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "products")
data class Product(
   @PrimaryKey val barCode: String,
   val name: String,
   val category: String,
   var expiry_date: Date,
)
