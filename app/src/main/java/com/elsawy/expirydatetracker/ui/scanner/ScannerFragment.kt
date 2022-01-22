package com.elsawy.expirydatetracker.ui.scanner

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.elsawy.expirydatetracker.BaseApplication
import com.elsawy.expirydatetracker.databinding.FragmentScannerBinding
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.coroutines.launch
import java.io.IOException

class ScannerFragment : Fragment() {

   private lateinit var cameraSource: CameraSource
   private lateinit var barcodeDetector: BarcodeDetector

   private var _binding: FragmentScannerBinding? = null
   private val binding get() = _binding!!

   private val viewModel: ScannerViewModel by viewModels {
      ScannerViewModelFactory(requireActivity().application,
         (requireContext().applicationContext as BaseApplication).productRepository)
   }

   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?,
   ): View? {
      _binding = FragmentScannerBinding.inflate(inflater, container, false)

      if (isCameraNotGranted()) {
         askForCameraPermission()
      } else {
         setupControls()
      }

      binding.viewModel = viewModel
      binding.lifecycleOwner = viewLifecycleOwner

      return binding.root
   }

   private fun isCameraNotGranted() = ContextCompat.checkSelfPermission(
      this.requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED

   private fun setupControls() {
      barcodeDetector =
         BarcodeDetector.Builder(requireContext()).setBarcodeFormats(Barcode.ALL_FORMATS).build()

      cameraSource = CameraSource.Builder(requireContext(), barcodeDetector)
         .setRequestedPreviewSize(640, 480)
         .setAutoFocusEnabled(true)
         .build()

      binding.cameraSurfaceView.holder.addCallback(surfaceHolderCallback())
      barcodeDetector.setProcessor(barCodeProcessor())
   }

   private fun barCodeProcessor() = object : Detector.Processor<Barcode> {
      override fun release() {}
      override fun receiveDetections(detections: Detector.Detections<Barcode>) {
         val barcodes = detections.detectedItems
         if (barcodes.size() == 1) {
            val scannedValue = barcodes.valueAt(0).rawValue

            lifecycleScope.launch {
               viewModel.setBarCode(scannedValue)
               cameraSource.stop()
            }
         }
      }
   }

   private fun surfaceHolderCallback() = object : SurfaceHolder.Callback {
      @SuppressLint("MissingPermission")
      override fun surfaceCreated(holder: SurfaceHolder) {
         try {
            cameraSource.start(holder)
         } catch (e: IOException) {
            e.printStackTrace()
         }
      }

      @SuppressLint("MissingPermission")
      override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
         try {
            cameraSource.start(holder)
         } catch (e: IOException) {
            e.printStackTrace()
         }
      }

      override fun surfaceDestroyed(holder: SurfaceHolder) {
         cameraSource.stop()
      }
   }

   private fun askForCameraPermission() {
      ActivityCompat.requestPermissions(
         requireActivity(),
         arrayOf(android.Manifest.permission.CAMERA),
         requestCodeCameraPermission
      )
   }

   fun onPermissionGranted() {
      setupControls()
   }

   override fun onDestroyView() {
      super.onDestroyView()
      if (::cameraSource.isInitialized)
         cameraSource.stop()
      _binding = null
   }

   companion object {
      const val requestCodeCameraPermission = 1001
   }
}