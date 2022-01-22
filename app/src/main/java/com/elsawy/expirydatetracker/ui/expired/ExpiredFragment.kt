package com.elsawy.expirydatetracker.ui.expired

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.elsawy.expirydatetracker.BaseApplication
import com.elsawy.expirydatetracker.R
import com.elsawy.expirydatetracker.databinding.FragmentExpiredBinding
import com.elsawy.expirydatetracker.ui.ProductAdapter
import com.elsawy.expirydatetracker.ui.home.HomeViewModel
import com.elsawy.expirydatetracker.ui.home.HomeViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ExpiredFragment : Fragment() {

   private var _binding: FragmentExpiredBinding? = null
   private val binding get() = _binding!!
   private val adapter by lazy { ProductAdapter() }

   private val viewModel: ExpiredViewModel by viewModels {
      ExpiredViewModelFactory((requireContext().applicationContext as BaseApplication).productRepository)
   }

   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?,
   ): View? {
      _binding = FragmentExpiredBinding.inflate(inflater, container, false)

      binding.expiredList.adapter = adapter
      subscribeUi()

      return binding.root
   }

   private fun subscribeUi() {
      lifecycleScope.launch {
         viewModel.getExpiredProducts().collectLatest { result ->
            adapter.submitList(result)
         }
      }
   }

   override fun onDestroyView() {
      super.onDestroyView()
      _binding = null
   }
}