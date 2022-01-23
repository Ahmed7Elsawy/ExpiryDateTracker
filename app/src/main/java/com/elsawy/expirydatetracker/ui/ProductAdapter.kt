package com.elsawy.expirydatetracker.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.elsawy.expirydatetracker.R
import com.elsawy.expirydatetracker.data.Product
import com.elsawy.expirydatetracker.databinding.ProductItemBinding

class ProductAdapter(private val longClickListener: ProductListener = ProductListener { false }) :
   ListAdapter<Product, ProductAdapter.ViewHolder>(
      ProductDiffCallback()
   ) {
   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      return ViewHolder(
         DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.product_item,
            parent,
            false
         )
      )
   }

   override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      holder.bind(longClickListener, getItem(position))
   }

   class ViewHolder(
      private val binding: ProductItemBinding,
   ) : RecyclerView.ViewHolder(binding.root) {
      fun bind(longClickListener: ProductListener, currentProduct: Product) {
         with(binding) {
            longClickListner = longClickListener
            product = currentProduct
            executePendingBindings()
         }
      }
   }
}

class ProductListener(val longClickListener: (product: Product) -> Boolean) {
   fun onLongClick(product: Product) = longClickListener(product)
}

private class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
   override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
      return oldItem.barCode == newItem.barCode
   }

   override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
      return oldItem == newItem
   }

}
