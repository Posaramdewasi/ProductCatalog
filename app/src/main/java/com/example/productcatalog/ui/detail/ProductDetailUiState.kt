package com.example.productcatalog.ui.detail

import com.example.productcatalog.data.model.ProductDto

sealed class ProductDetailUiState {
    object Loading : ProductDetailUiState()
    data class Success(val product: ProductDto) : ProductDetailUiState()
    data class Error(val message: String) : ProductDetailUiState()
}
