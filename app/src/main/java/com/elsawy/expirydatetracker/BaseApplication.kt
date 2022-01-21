package com.elsawy.expirydatetracker

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.elsawy.expirydatetracker.data.ProductRepository

class BaseApplication : Application() {

   val productRepository: ProductRepository
      get() = ServiceLocator.provideProductRepository(this)

   override fun onCreate() {
      super.onCreate()
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
         val name = getString(R.string.channel_name)
         val descriptionText = getString(R.string.channel_description)
         val importance = NotificationManager.IMPORTANCE_DEFAULT
         val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
         }
         // Register the channel with the system
         val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
         notificationManager.createNotificationChannel(channel)
      }
   }

   companion object {
      const val CHANNEL_ID = "expiry_reminder_id"
   }
}
