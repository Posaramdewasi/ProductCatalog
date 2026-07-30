package com.example.productcatalog.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productcatalog.domain.repository.ProductRepository
import com.example.productcatalog.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val repository: ProductRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductDetailUiState>(ProductDetailUiState.Loading)
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    private val productId: Int? = savedStateHandle["product_id"]

    init {
        productId?.let {
            getProductDetails(it)
        } ?: run {
            _uiState.value = ProductDetailUiState.Error("Invalid Product ID")
        }
    }

    fun getProductDetails(id: Int = productId ?: -1) {
        if (id == -1) return
        viewModelScope.launch {
            repository.getProductDetails(id).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.value = ProductDetailUiState.Loading
                    }
                    is Resource.Success -> {
                        result.data?.let {
                            _uiState.value = ProductDetailUiState.Success(it)
                        } ?: run {
                            _uiState.value = ProductDetailUiState.Error("Product not found")
                        }
                    }
                    is Resource.Error -> {
                        _uiState.value = ProductDetailUiState.Error(result.message ?: "Unknown error")
                    }
                }
            }
        }
    }
}
