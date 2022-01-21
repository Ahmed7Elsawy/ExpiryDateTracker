package com.elsawy.expirydatetracker.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface ProductDao {

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   fun insertProduct(product: Product)

   @Query("SELECT * FROM products where expiry_date > :currentDate ORDER BY expiry_date")
   fun getUnExpiredProducts(currentDate:Date=Date(System.currentTimeMillis())): Flow<List<Product>>

   @Query("SELECT * FROM products where expiry_date <= :currentDate ORDER BY expiry_date DESC")
   fun getExpiredProducts(currentDate:Date=Date(System.currentTimeMillis())): Flow<List<Product>>

   @Query("SELECT * FROM products where barCode = :barcode")
   fun getProductByBarCode(barcode: String): Flow<Product>

}