package com.elsawy.expirydatetracker.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class ProductDaoTest {

   private lateinit var database: ProductDatabase

   // Executes each task synchronously using Architecture Components.
   @get:Rule
   var instantExecutorRule = InstantTaskExecutorRule()

   @Before
   fun initDb() {
      database = Room.inMemoryDatabaseBuilder(
         ApplicationProvider.getApplicationContext(),
         ProductDatabase::class.java
      ).build()
   }

   @After
   fun closeDb() = database.close()

   @Test
   fun insertProductAndGetByBarCode() = runBlockingTest {
      // GIVEN - insert a product
      val product = Product("10010001", "Chocolate", "Food", Date())
      database.productDao().insertProduct(product)

      // WHEN - Get product by barcode from the database
      val loaded = database.productDao().getProductByBarCode(product.barCode).first()

      // THEN - The loaded data contains the expected values
      MatcherAssert.assertThat<Product>(loaded as Product, CoreMatchers.notNullValue())
      MatcherAssert.assertThat(loaded.barCode, `is`(product.barCode))
      MatcherAssert.assertThat(loaded.name, `is`(product.name))
      MatcherAssert.assertThat(loaded.category, `is`(product.category))
      MatcherAssert.assertThat(loaded.expiry_date, `is`(product.expiry_date))
   }

   @Test
   fun insertSomeProductsAndGetUnExpiredProducts() = runBlockingTest {
      //GIVEN - insert a product
      val product1 = Product("10010001", "Chocolate", "Food", getDate(5))
      val product2 = Product("10010002", "Cheese", "Food", getDate(-5))
      val product3 = Product("10010003", "Cola", "Drink", getDate(-2))
      val product4 = Product("10010004", "Juice", "Drink", getDate(2))
      database.productDao().insertProduct(product1)
      database.productDao().insertProduct(product2)
      database.productDao().insertProduct(product3)
      database.productDao().insertProduct(product4)

      // WHEN - Get the unexpired products from the database
      val loaded = database.productDao().getUnExpiredProducts().first()

      // THEN - The loaded data contains the expected values
      MatcherAssert.assertThat(loaded.size, `is`(2))
      MatcherAssert.assertThat(loaded[0], `is`(product4))
      MatcherAssert.assertThat(loaded[1], `is`(product1))
   }

   @Test
   fun insertSomeProductsAndGetExpiredProducts() = runBlockingTest {
      //GIVEN - insert a product
      val product1 = Product("10010001", "Chocolate", "Food", getDate(5))
      val product2 = Product("10010002", "Cheese", "Food", getDate(-5))
      val product3 = Product("10010003", "Cola", "Drink", getDate(-2))
      val product4 = Product("10010004", "Juice", "Drink", getDate(2))
      database.productDao().insertProduct(product1)
      database.productDao().insertProduct(product2)
      database.productDao().insertProduct(product3)
      database.productDao().insertProduct(product4)

      // WHEN - Get expired products from the database
      val loaded = database.productDao().getExpiredProducts().first()

      // THEN - The loaded data contains the expected values
      MatcherAssert.assertThat(loaded.size, `is`(2))
      MatcherAssert.assertThat(loaded[0], `is`(product3))
      MatcherAssert.assertThat(loaded[1], `is`(product2))
   }

   private fun getDate(days: Int): Date {
      val cal = Calendar.getInstance()
      cal.add(Calendar.DAY_OF_YEAR, days)
      return Date(cal.timeInMillis)
   }

}