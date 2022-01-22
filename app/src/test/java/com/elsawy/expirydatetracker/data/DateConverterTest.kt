package com.elsawy.expirydatetracker.data

import org.junit.Assert
import org.junit.Test
import java.util.*

class DateConverterTest {

   private val timeStamp = 1642888986L

   @Test
   fun calendarToDatestamp() {
      Assert.assertEquals(timeStamp, DateConverter().dateToTimestamp(Date(timeStamp)))
   }

   @Test
   fun datestampToCalendar() {
      Assert.assertEquals(Date(timeStamp), DateConverter().fromTimestamp(timeStamp))
   }

}