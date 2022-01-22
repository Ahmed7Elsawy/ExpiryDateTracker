package com.elsawy.expirydatetracker.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.elsawy.expirydatetracker.data.ProductRepository

class HomeViewModel(private val productRepository: ProductRepository) : ViewModel() {

   suspend fun getUnexpiredProducts() = productRepository.getUnExpiredProducts()

}

class HomeViewModelFactory(
   private val productRepository: ProductRepository,
) : ViewModelProvider.Factory {
   override fun <T : ViewModel> create(modelClass: Class<T>): T {
      return if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
         HomeViewModel(productRepository) as T
      } else {
         throw IllegalArgumentException("Unknown ViewModel class")
      }
   }
}