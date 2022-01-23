package com.elsawy.expirydatetracker

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.elsawy.expirydatetracker.data.ProductDatabase
import com.elsawy.expirydatetracker.data.ProductRepository
import kotlinx.coroutines.runBlocking

object ServiceLocator {

   private val lock = Any()
   private var database: ProductDatabase? = null

   @Volatile
   var productsRepository: ProductRepository? = null
      @VisibleForTesting set

   fun provideProductRepository(context: Context): ProductRepository {
      synchronized(this) {
         return productsRepository ?: createProductRepository(context)
      }
   }

   private fun createProductRepository(context: Context): ProductRepository {
      val newRepo = ProductRepository(getProductDataBase(context).productDao())
      productsRepository = newRepo
      return newRepo
   }

   private fun getProductDataBase(context: Context) = database ?: createDataBase(context)

   private fun createDataBase(context: Context): ProductDatabase {
      val result = Room.databaseBuilder(
         context.applicationContext,
         ProductDatabase::class.java, "Product.db"
      ).build()
      database = result
      return result
   }


   @VisibleForTesting
   fun resetRepository() {
      synchronized(lock) {

         // Clear all data to avoid test pollution.
         database?.apply {
            clearAllTables()
            close()
         }
         database = null
         productsRepository = null
      }
   }

}
