package com.elsawy.expirydatetracker.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Product::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class ProductDatabase : RoomDatabase() {
   abstract fun productDao(): ProductDao
}