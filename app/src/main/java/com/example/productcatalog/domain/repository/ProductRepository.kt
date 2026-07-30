package com.example.productcatalog.domain.repository

import com.example.productcatalog.data.model.ProductDto
import com.example.productcatalog.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProducts(): Flow<Resource<List<ProductDto>>>
    fun getProductDetails(id: Int): Flow<Resource<ProductDto>>
}
