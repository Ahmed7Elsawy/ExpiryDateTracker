package com.elsawy.expirydatetracker.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

   private const val HOUR_MILLI_SECONDS = 3600000L
   private const val DAY_MILLI_SECONDS = 86400000L

   @JvmStatic
   fun dateToString(date: Date): String {

      if (isExpiredDate(date)) {
         return "expired at ${formatTheDate(date)}"
      }
      if (isLessThanOneDay(date)) {
         return "less than ${getRemainHours(date)} hours"
      }
      return "expiry date ${formatTheDate(date)}"
   }

   @JvmStatic
   fun isLessThanOneDay(date: Date): Boolean {
      val currentDate = System.currentTimeMillis()
      return DAY_MILLI_SECONDS >= date.time - currentDate
   }

   private fun formatTheDate(date: Date): String {
      val formatter = SimpleDateFormat("dd/MM/yyyy");
      return formatter.format(date)
   }

   private fun getRemainHours(date: Date): Long {
      val currentDate = System.currentTimeMillis()
      return (date.time - currentDate) / HOUR_MILLI_SECONDS + 1
   }

   private fun isExpiredDate(date: Date): Boolean {
      val currentDate = System.currentTimeMillis()
      return currentDate > date.time
   }

   fun getDateForNextHours(hours: Long): Date {
      val time = System.currentTimeMillis() + hours * HOUR_MILLI_SECONDS
      return Date(time)
   }

}