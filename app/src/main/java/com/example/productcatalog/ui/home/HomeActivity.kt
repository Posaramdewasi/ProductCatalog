package com.example.productcatalog.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.productcatalog.databinding.ActivityHomeBinding
import com.example.productcatalog.ui.adapter.ProductAdapter
import com.example.productcatalog.ui.detail.ProductDetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private val adapter by lazy {
        ProductAdapter { product ->
            val intent = Intent(this, ProductDetailActivity::class.java).apply {
                putExtra(ProductDetailActivity.EXTRA_PRODUCT_ID, product.id)
            }
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupListeners()
        observeUiState()
    }

    private fun setupRecyclerView() {
        val layoutManager = GridLayoutManager(this, 2)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
        
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                ) {
                    viewModel.loadNextPage()
                }
            }
        })
    }

    private fun setupListeners() {
        binding.retryButton.setOnClickListener {
            viewModel.retry()
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

    private fun handleUiState(state: HomeUiState) {
        when (state) {
            is HomeUiState.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
                binding.errorLayout.visibility = View.GONE
                binding.emptyLayout.visibility = View.GONE
                binding.bottomProgressBar.visibility = View.GONE
            }
            is HomeUiState.Success -> {
                binding.progressBar.visibility = View.GONE
                binding.errorLayout.visibility = View.GONE
                binding.emptyLayout.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                adapter.submitList(state.products)
                binding.bottomProgressBar.visibility = if (state.isLazyLoading) View.VISIBLE else View.GONE
            }
            is HomeUiState.Empty -> {
                binding.progressBar.visibility = View.GONE
                binding.recyclerView.visibility = View.GONE
                binding.errorLayout.visibility = View.GONE
                binding.emptyLayout.visibility = View.VISIBLE
                binding.bottomProgressBar.visibility = View.GONE
            }
            is HomeUiState.Error -> {
                binding.progressBar.visibility = View.GONE
                binding.recyclerView.visibility = View.GONE
                binding.emptyLayout.visibility = View.GONE
                binding.errorLayout.visibility = View.VISIBLE
                binding.errorText.text = state.message
                binding.bottomProgressBar.visibility = View.GONE
            }
        }
    }
}
