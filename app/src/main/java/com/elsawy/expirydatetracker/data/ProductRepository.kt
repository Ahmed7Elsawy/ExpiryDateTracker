package com.elsawy.expirydatetracker.data

import kotlinx.coroutines.flow.Flow

class ProductRepository(private val productDao: ProductDao) {
   suspend fun insertProduct(product: Product) =
      productDao.insertProduct(product)

   suspend fun getUnExpiredProducts(): Flow<List<Product>> =
      productDao.getUnExpiredProducts()

   suspend fun getExpiredProducts(): Flow<List<Product>> =
      productDao.getExpiredProducts()

   suspend fun getProductByBarCode(barcode: String): Flow<Product> =
      productDao.getProductByBarCode(barcode)
}