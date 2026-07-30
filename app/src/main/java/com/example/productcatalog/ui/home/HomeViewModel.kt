package com.example.productcatalog.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productcatalog.data.model.ProductDto
import com.example.productcatalog.domain.repository.ProductRepository
import com.example.productcatalog.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var allProducts = listOf<ProductDto>()
    private var displayedProducts = mutableListOf<ProductDto>()
    private val PAGE_SIZE = 6
    private var currentPage = 0

    init {
        getProducts()
    }

    fun getProducts() {
        viewModelScope.launch {
            repository.getProducts().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        if (displayedProducts.isEmpty()) {
                            _uiState.value = HomeUiState.Loading
                        }
                    }
                    is Resource.Success -> {
                        allProducts = result.data ?: emptyList()
                        if (allProducts.isEmpty()) {
                            _uiState.value = HomeUiState.Empty
                        } else {
                            resetPagination()
                            loadNextPage()
                        }
                    }
                    is Resource.Error -> {
                        if (displayedProducts.isEmpty()) {
                            _uiState.value = HomeUiState.Error(result.message ?: "Unknown error")
                        }
                    }
                }
            }
        }
    }

    fun loadNextPage() {
        if (_uiState.value is HomeUiState.Loading && displayedProducts.isNotEmpty()) return
        
        val startIndex = currentPage * PAGE_SIZE
        if (startIndex >= allProducts.size) return

        val endIndex = minOf(startIndex + PAGE_SIZE, allProducts.size)
        val nextItems = allProducts.subList(startIndex, endIndex)

        displayedProducts.addAll(nextItems)
        currentPage++

        _uiState.value = HomeUiState.Success(
            products = displayedProducts.toList(),
            isLazyLoading = startIndex + PAGE_SIZE < allProducts.size
        )
    }

    private fun resetPagination() {
        displayedProducts.clear()
        currentPage = 0
    }

    fun retry() {
        getProducts()
    }
}
