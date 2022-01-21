package com.elsawy.expirydatetracker.ui.scanner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elsawy.expirydatetracker.R
import com.elsawy.expirydatetracker.databinding.FragmentHomeBinding
import com.elsawy.expirydatetracker.databinding.FragmentScannerBinding

class ScannerFragment : Fragment() {

   private var _binding: FragmentScannerBinding? = null
   private val binding get() = _binding!!

   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?,
   ): View? {
      _binding = FragmentScannerBinding.inflate(inflater, container, false)
      return binding.root
   }

   override fun onDestroyView() {
      super.onDestroyView()
      _binding = null
   }
}