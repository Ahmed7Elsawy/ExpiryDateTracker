package com.elsawy.expirydatetracker.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.elsawy.expirydatetracker.R
import com.elsawy.expirydatetracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      val binding = ActivityMainBinding.inflate(layoutInflater)
      setContentView(binding.root)

      val navController = findNavController(R.id.nav_host)
      binding.bottomNavigationView.setupWithNavController(navController)
   }
}