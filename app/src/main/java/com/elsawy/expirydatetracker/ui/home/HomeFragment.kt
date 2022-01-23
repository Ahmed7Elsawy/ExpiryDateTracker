package com.elsawy.expirydatetracker.ui.home

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
import com.elsawy.expirydatetracker.databinding.FragmentHomeBinding
import com.elsawy.expirydatetracker.ui.ProductAdapter
import com.elsawy.expirydatetracker.ui.ProductListener
import com.elsawy.expirydatetracker.ui.scanner.ScannerViewModel
import com.elsawy.expirydatetracker.ui.scanner.ScannerViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

   private var _binding: FragmentHomeBinding? = null
   private val binding get() = _binding!!
   private lateinit var adapter: ProductAdapter

   private val viewModel: HomeViewModel by viewModels {
      HomeViewModelFactory(requireActivity().application,
         (requireContext().applicationContext as BaseApplication).productRepository)
   }

   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?,
   ): View? {
      _binding = FragmentHomeBinding.inflate(inflater, container, false)

      adapter = ProductAdapter(
         ProductListener { product ->
            val dialog = ChangeDateDialogFragment(product)
            dialog.show(parentFragmentManager, "ChangeDateDialogFragment")
            true
         }
      )
      binding.unexpiredList.adapter = adapter

      subscribeUi()

      return binding.root
   }

   private fun subscribeUi() {
      lifecycleScope.launch {
         viewModel.getUnexpiredProducts().collectLatest { result ->
            adapter.submitList(result)
         }
      }
   }

   override fun onDestroyView() {
      super.onDestroyView()
      _binding = null
   }
}