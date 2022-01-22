package com.elsawy.expirydatetracker.ui.expired

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.elsawy.expirydatetracker.data.ProductRepository

class ExpiredViewModel(private val productRepository: ProductRepository) : ViewModel() {

   suspend fun getExpiredProducts() = productRepository.getExpiredProducts()

}

class ExpiredViewModelFactory(
   private val productRepository: ProductRepository,
) : ViewModelProvider.Factory {
   override fun <T : ViewModel> create(modelClass: Class<T>): T {
      return if (modelClass.isAssignableFrom(ExpiredViewModel::class.java)) {
         ExpiredViewModel(productRepository) as T
      } else {
         throw IllegalArgumentException("Unknown ViewModel class")
      }
   }
}