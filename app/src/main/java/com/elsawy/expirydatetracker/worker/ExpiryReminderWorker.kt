package com.elsawy.expirydatetracker.worker

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.elsawy.expirydatetracker.BaseApplication
import com.elsawy.expirydatetracker.R
import com.elsawy.expirydatetracker.ui.MainActivity

class ExpiryReminderWorker(
   context: Context,
   workerParams: WorkerParameters,
) : Worker(context, workerParams) {

   override fun doWork(): Result {

      val notificationId = 17
      val pendingIntent: PendingIntent = pendingIntent()

      val productName = inputData.getString(nameKey)

      val builder = NotificationCompat.Builder(applicationContext, BaseApplication.CHANNEL_ID)
         .setSmallIcon(R.drawable.ic_expired)
         .setContentTitle("Expiry Date Tracker")
         .setContentText("The $productName expired")
         .setPriority(NotificationCompat.PRIORITY_HIGH)
         .setContentIntent(pendingIntent)
         .setAutoCancel(true)

      with(NotificationManagerCompat.from(applicationContext)) {
         notify(notificationId, builder.build())
      }

      return Result.success()
   }

   private fun pendingIntent(): PendingIntent {
      val intent = Intent(applicationContext, MainActivity::class.java).apply {
         flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
      }

      return PendingIntent
         .getActivity(applicationContext, 0, intent, 0)
   }

   companion object {
      const val nameKey = "NAME"
   }
}