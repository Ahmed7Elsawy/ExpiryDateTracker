package com.elsawy.expirydatetracker.ui.home

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.elsawy.expirydatetracker.R
import com.elsawy.expirydatetracker.ServiceLocator
import com.elsawy.expirydatetracker.data.Product
import com.elsawy.expirydatetracker.data.ProductRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*


@RunWith(AndroidJUnit4::class)
@LargeTest
@ExperimentalCoroutinesApi
class HomeFragmentTest {

   private lateinit var repository: ProductRepository
   private val DAY_MILLI_SECONDS = 86400000L
   private val HOUR_MILLI_SECONDS = 3600000L

   @Before
   fun setup() {
      repository =
         ServiceLocator.provideProductRepository(getApplicationContext())
      runBlocking {
         repository.deleteAllProducts()
      }
      launchFragmentInContainer<HomeFragment>(Bundle(), R.style.Theme_ExpiryDateTracker)
   }

   @After
   fun reset() {
      ServiceLocator.resetRepository()
   }

   @Test
   fun insertTheProductsAndAssertThereOrderByExpiryDate() = runBlockingTest {

      // GIVEN - add three products with different expiry date in the repository
      val product1 = Product("111", "product1", "food",
         Date(System.currentTimeMillis() + DAY_MILLI_SECONDS * 4))
      val product2 = Product("222", "product2", "food",
         Date(System.currentTimeMillis() + DAY_MILLI_SECONDS * 2))
      val product3 = Product("333", "product3", "drink",
         Date(System.currentTimeMillis() + DAY_MILLI_SECONDS * 3))

      repository.insertProduct(product1)
      repository.insertProduct(product2)
      repository.insertProduct(product3)


      // THEN - Assert Order By Expiry Date
      onView(withId(R.id.unexpired_list))
         .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
         .check(ViewAssertions.matches(ViewMatchers.hasDescendant(ViewMatchers.withText("product2"))))

      onView(withId(R.id.unexpired_list))
         .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(1))
         .check(ViewAssertions.matches(ViewMatchers.hasDescendant(ViewMatchers.withText("product3"))))

      onView(withId(R.id.unexpired_list))
         .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(2))
         .check(ViewAssertions.matches(ViewMatchers.hasDescendant(ViewMatchers.withText("product1"))))

   }


   @Test
   fun checkDialogDoNotDisplayWhenExpireDateLessThanDay() = runBlockingTest {

      // GIVEN - Add product with expiry date less than one day to the repository
      val product1 = Product("111", "product1", "food",
         Date(System.currentTimeMillis() + HOUR_MILLI_SECONDS * 4))
      repository.insertProduct(product1)

      // WHEN - long click on the item
      onView(withId(R.id.unexpired_list))
         .perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
            hasDescendant(withText("product1")), ViewActions.longClick()))

      // THEN - Assert dialog won't be displayed
      onView(withText(R.string.change_date)).check(doesNotExist())
   }

   @Test
   fun checkDialogDisplayWhenExpireDateMoreThanDay() = runBlockingTest {

      // GIVEN - Add product with expiry date more than one day to the repository
      val product1 = Product("111", "product1", "food",
         Date(System.currentTimeMillis() + DAY_MILLI_SECONDS * 4))
      repository.insertProduct(product1)

      // WHEN - long click on the item
      onView(withId(R.id.unexpired_list))
         .perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
            hasDescendant(withText("product1")), ViewActions.longClick()))

      // THEN - Assert dialog is displayed
      onView(withText(R.string.change_date)).check(matches(isDisplayed()));
   }


   @Test
   fun changeDateAndAssertTheOrderAndDateChanged() = runBlockingTest {

      // GIVEN - add three products with different expiry date to the repository
      val product1 = Product("111", "product1", "food",
         Date(System.currentTimeMillis() + DAY_MILLI_SECONDS * 4))
      val product2 = Product("222", "product2", "food",
         Date(System.currentTimeMillis() + DAY_MILLI_SECONDS * 2))
      val product3 = Product("333", "product3", "drink",
         Date(System.currentTimeMillis() + DAY_MILLI_SECONDS * 3))
      repository.insertProduct(product1)
      repository.insertProduct(product2)
      repository.insertProduct(product3)


      // WHEN - Long click on item and change it's expiry date
      onView(withId(R.id.unexpired_list))
         .perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
            hasDescendant(withText("product1")), ViewActions.longClick()))

      onView(withText("12 hours")).perform(click())


      // THEN - Assert Expiry Date was changed and the order of the item in the list
      onView(withId(R.id.unexpired_list))
         .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
         .check(matches(hasDescendant(withText("product1"))))

      onView(withId(R.id.unexpired_list))
         .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
         .check(matches(hasDescendant(withText("less than 12 hours"))))

   }

}