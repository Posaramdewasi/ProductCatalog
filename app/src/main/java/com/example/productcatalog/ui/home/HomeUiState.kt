package com.example.productcatalog.ui.home

import com.example.productcatalog.data.model.ProductDto

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val products: List<ProductDto>, val isLazyLoading: Boolean = false) : HomeUiState()
    object Empty : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}
