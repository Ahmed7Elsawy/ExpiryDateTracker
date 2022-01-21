package com.elsawy.expirydatetracker.ui.expired

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elsawy.expirydatetracker.R
import com.elsawy.expirydatetracker.databinding.FragmentExpiredBinding

class ExpiredFragment : Fragment() {

   private var _binding: FragmentExpiredBinding? = null
   private val binding get() = _binding!!

   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?,
   ): View? {
      _binding = FragmentExpiredBinding.inflate(inflater, container, false)
      return binding.root
   }

   override fun onDestroyView() {
      super.onDestroyView()
      _binding = null
   }
}