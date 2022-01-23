package com.elsawy.expirydatetracker.ui.home

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.elsawy.expirydatetracker.data.Product
import com.elsawy.expirydatetracker.data.ProductRepository
import com.elsawy.expirydatetracker.utils.DateUtils.getDateForNextHours
import com.elsawy.expirydatetracker.worker.ExpiryReminderWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class HomeViewModel(
   private val application: Application,
   private val productRepository: ProductRepository,
) : ViewModel() {

   suspend fun getUnexpiredProducts() = productRepository.getUnExpiredProducts()

   fun updateProductDate(product: Product, hours: Long) {
      product.expiry_date = getDateForNextHours(hours)
      viewModelScope.launch(Dispatchers.IO) {
         productRepository.insertProduct(product)
      }
      scheduleReminder(hours, product.name)
   }

   private fun scheduleReminder(
      hours: Long,
      productName: String,
   ) {
      val data = workDataOf(ExpiryReminderWorker.nameKey to productName)

      val workRequest =
         OneTimeWorkRequestBuilder<ExpiryReminderWorker>()
            .setInputData(data)
            .setInitialDelay(hours, TimeUnit.HOURS)
            .build()

      val workManager = WorkManager.getInstance(application.applicationContext)
      workManager.enqueueUniqueWork(
         productName,
         ExistingWorkPolicy.REPLACE,
         workRequest)
   }

}

class HomeViewModelFactory(
   private val application: Application,
   private val productRepository: ProductRepository,
) : ViewModelProvider.Factory {
   override fun <T : ViewModel> create(modelClass: Class<T>): T {
      return if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
         HomeViewModel(application, productRepository) as T
      } else {
         throw IllegalArgumentException("Unknown ViewModel class")
      }
   }
}