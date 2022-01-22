package com.elsawy.expirydatetracker.ui.scanner

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
import com.elsawy.expirydatetracker.worker.ExpiryReminderWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit

class ScannerViewModel(
   private val application: Application,
   private val productRepository: ProductRepository,
) : ViewModel() {

   private val _isNew = MutableStateFlow(false)
   val isNew: StateFlow<Boolean> = _isNew
   private val _isExist = MutableStateFlow(false)
   val isExist: StateFlow<Boolean> = _isExist

   private val barCode = MutableStateFlow("")
   val product = MutableStateFlow<Product?>(null)
   val name = MutableStateFlow("")
   val category = MutableStateFlow("")
   private val currentCalendar = Calendar.getInstance()
   val expireDay = MutableStateFlow(currentCalendar.get(Calendar.DAY_OF_MONTH))
   val expireMonth = MutableStateFlow(currentCalendar.get(Calendar.MONTH))
   val expireYear = MutableStateFlow(currentCalendar.get(Calendar.YEAR))

   fun setBarCode(scannedValue: String) {
      barCode.value = scannedValue
      getProduct(scannedValue)
   }

   private fun getProduct(barCode: String) {
      viewModelScope.launch(Dispatchers.IO) {
         productRepository.getProductByBarCode(barCode).collectLatest {
            if (it == null) {
               _isNew.value = true
               _isExist.value = false
            } else {
               product.value = it
               _isExist.value = true
               _isNew.value = false
            }
         }
      }
   }

   fun onAddClick() {
      if (name.value == "" || category.value == "")
         return

      val calendar = Calendar.getInstance()
      calendar.set(expireYear.value, expireMonth.value, expireDay.value)
      val newProduct = Product(barCode.value, name.value, category.value, calendar.time)

      viewModelScope.launch(Dispatchers.IO) {
         productRepository.insertProduct(newProduct)
      }
      scheduleReminder(newProduct.expiry_date, newProduct.name)
   }


   fun scheduleReminder(
      date: Date,
      productName: String,
   ) {
      val data = workDataOf(ExpiryReminderWorker.nameKey to productName)

      val currentTime = System.currentTimeMillis()

      val fiveMinutes = 5 * 60 * 1000
      val delayToPass = date.time + fiveMinutes - currentTime

      val workRequest =
         OneTimeWorkRequestBuilder<ExpiryReminderWorker>()
            .setInputData(data)
            .setInitialDelay(delayToPass, TimeUnit.MILLISECONDS)
            .build()

      val workManager = WorkManager.getInstance(application.applicationContext)
      workManager.enqueueUniqueWork(
         productName,
         ExistingWorkPolicy.REPLACE,
         workRequest)
   }

}

class ScannerViewModelFactory(
   private val application: Application,
   private val productRepository: ProductRepository,
) : ViewModelProvider.Factory {
   override fun <T : ViewModel> create(modelClass: Class<T>): T {
      return if (modelClass.isAssignableFrom(ScannerViewModel::class.java)) {
         ScannerViewModel(application, productRepository) as T
      } else {
         throw IllegalArgumentException("Unknown ViewModel class")
      }
   }
}