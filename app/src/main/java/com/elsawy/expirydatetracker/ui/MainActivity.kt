package com.elsawy.expirydatetracker.ui

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.elsawy.expirydatetracker.R
import com.elsawy.expirydatetracker.databinding.ActivityMainBinding
import com.elsawy.expirydatetracker.ui.scanner.ScannerFragment
import com.elsawy.expirydatetracker.ui.scanner.ScannerFragment.Companion.requestCodeCameraPermission

class MainActivity : AppCompatActivity() {
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      val binding = ActivityMainBinding.inflate(layoutInflater)
      setContentView(binding.root)

      val navController = findNavController(R.id.nav_host)
      binding.bottomNavigationView.setupWithNavController(navController)
   }

   override fun onRequestPermissionsResult(
      requestCode: Int,
      permissions: Array<out String>,
      grantResults: IntArray,
   ) {
      super.onRequestPermissionsResult(requestCode, permissions, grantResults)
      Log.d("PermissionsResult", "Activity")

      if (requestCode == requestCodeCameraPermission && grantResults.isNotEmpty()) {
         if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            val navHostFragment =
               supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
            val currentFragment =
               navHostFragment.childFragmentManager.primaryNavigationFragment as? ScannerFragment
            Log.d("PermissionsResult", "${currentFragment == null}")
            currentFragment?.onPermissionGranted()
         }
      }

   }
}