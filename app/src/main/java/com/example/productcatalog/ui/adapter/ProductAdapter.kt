package com.example.productcatalog.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.productcatalog.R
import com.example.productcatalog.data.model.ProductDto
import com.example.productcatalog.databinding.ItemProductBinding

class ProductAdapter(private val onProductClick: (ProductDto) -> Unit) :
    ListAdapter<ProductDto, ProductAdapter.ProductViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onProductClick(getItem(position))
                }
            }
        }

        fun bind(product: ProductDto) {
            binding.productTitle.text = product.title
            binding.productCategory.text = product.category
            binding.productPrice.text = "$${product.price}"
            binding.productRating.text = "${product.rating.rate} (${product.rating.count})"
            binding.productImage.load(product.image) {
                crossfade(true)
                placeholder(R.drawable.ic_launcher_foreground) // Use a generic placeholder
                error(R.drawable.ic_launcher_background)
            }
        }
    }

    class ProductDiffCallback : DiffUtil.ItemCallback<ProductDto>() {
        override fun areItemsTheSame(oldItem: ProductDto, newItem: ProductDto): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductDto, newItem: ProductDto): Boolean {
            return oldItem == newItem
        }
    }
}
