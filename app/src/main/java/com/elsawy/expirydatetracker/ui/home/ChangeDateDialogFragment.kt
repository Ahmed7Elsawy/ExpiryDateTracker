package com.elsawy.expirydatetracker.ui.home

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.elsawy.expirydatetracker.BaseApplication
import com.elsawy.expirydatetracker.R
import com.elsawy.expirydatetracker.data.Product
import java.util.concurrent.TimeUnit

class ChangeDateDialogFragment(private val product: Product) : DialogFragment() {

   private val viewModel: HomeViewModel by viewModels {
      HomeViewModelFactory(
         requireActivity().application,
         (requireContext().applicationContext as BaseApplication).productRepository)
   }

   override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
      return activity?.let {
         val builder = AlertDialog.Builder(it)
            .setTitle(getString(R.string.change_date))
            .setItems(R.array.change_date_array) { _, position ->
               when (position) {
                  0 -> viewModel.updateProductDate(product, 6L)
                  1 -> viewModel.updateProductDate(product, 12L)
                  2 -> viewModel.updateProductDate(product, 18L)
                  3 -> viewModel.updateProductDate(product, 24L)
               }
            }
         builder.create()
      } ?: throw IllegalStateException("Activity cannot be null")
   }

}