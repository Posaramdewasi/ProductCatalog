package com.example.productcatalog.ui.detail

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.example.productcatalog.databinding.ActivityProductDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding
    private val viewModel: ProductDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupListeners()
        observeUiState()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupListeners() {
        binding.retryButton.setOnClickListener {
            viewModel.getProductDetails()
        }
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    handleUiState(state)
                }
            }
        }
    }

    private fun handleUiState(state: ProductDetailUiState) {
        when (state) {
            is ProductDetailUiState.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
                binding.contentScrollView.visibility = View.GONE
                binding.errorLayout.visibility = View.GONE
            }
            is ProductDetailUiState.Success -> {
                binding.progressBar.visibility = View.GONE
                binding.errorLayout.visibility = View.GONE
                binding.contentScrollView.visibility = View.VISIBLE
                
                val product = state.product
                binding.productTitle.text = product.title
                binding.productCategory.text = product.category
                binding.productPrice.text = "$${product.price}"
                binding.productRating.text = "${product.rating.rate} (${product.rating.count} reviews)"
                binding.productDescription.text = product.description
                binding.productImage.load(product.image) {
                    crossfade(true)
                }
            }
            is ProductDetailUiState.Error -> {
                binding.progressBar.visibility = View.GONE
                binding.contentScrollView.visibility = View.GONE
                binding.errorLayout.visibility = View.VISIBLE
                binding.errorText.text = state.message
            }
        }
    }
    
    companion object {
        const val EXTRA_PRODUCT_ID = "product_id"
    }
}
