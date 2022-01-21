package com.elsawy.expirydatetracker

import android.app.Application
import com.elsawy.expirydatetracker.data.ProductRepository

class ExpiryDateApplication : Application() {

   val productRepository: ProductRepository
      get() = ServiceLocator.provideProductRepository(this)

}
