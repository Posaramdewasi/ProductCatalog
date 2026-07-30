package com.example.productcatalog.data.repository

import com.example.productcatalog.data.api.ProductApi
import com.example.productcatalog.data.db.ProductDao
import com.example.productcatalog.data.mapper.toDto
import com.example.productcatalog.data.mapper.toEntity
import com.example.productcatalog.data.model.ProductDto
import com.example.productcatalog.domain.repository.ProductRepository
import com.example.productcatalog.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val api: ProductApi,
    private val dao: ProductDao
) : ProductRepository {

    override fun getProducts(): Flow<Resource<List<ProductDto>>> = flow {
        emit(Resource.Loading())

        // 1. Try fetching from network
        try {
            val response = api.getProducts()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                // 2. Success: Save to Room
                dao.deleteAllProducts()
                dao.insertProducts(body.map { it.toEntity() })
                emit(Resource.Success(body))
            } else {
                // Network error but handled by Retrofit (e.g. 404, 500)
                handleNetworkFailure(Resource.Error("HTTP Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            // 3. Network failure (No Internet, Timeout, etc.)
            val errorMessage = when (e) {
                is IOException -> "Network Error: Please check your internet connection"
                is HttpException -> "Unexpected HTTP Error: ${e.message()}"
                else -> "Unknown Error: ${e.localizedMessage ?: "An error occurred"}"
            }
            handleNetworkFailure(Resource.Error(errorMessage))
        }
    }

    private suspend fun kotlinx.coroutines.flow.FlowCollector<Resource<List<ProductDto>>>.handleNetworkFailure(
        error: Resource.Error<List<ProductDto>>
    ) {
        val cachedProducts = dao.getProducts()
        if (cachedProducts.isNotEmpty()) {
            emit(Resource.Success(cachedProducts.map { it.toDto() }))
        } else {
            emit(error)
        }
    }

    override fun getProductDetails(id: Int): Flow<Resource<ProductDto>> = flow {
        emit(Resource.Loading())

        try {
            val response = api.getProductDetails(id)
            val body = response.body()
            if (response.isSuccessful && body != null) {
                // Note: We don't necessarily need to cache single items if the full list is cached,
                // but we ensure Room is updated if this specific item was missing or different.
                dao.insertProducts(listOf(body.toEntity()))
                emit(Resource.Success(body))
            } else {
                handleDetailFailure(id, Resource.Error("HTTP Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            val errorMessage = when (e) {
                is IOException -> "Network Error: Please check your internet connection"
                is HttpException -> "Unexpected HTTP Error: ${e.message()}"
                else -> "Unknown Error: ${e.localizedMessage ?: "An error occurred"}"
            }
            handleDetailFailure(id, Resource.Error(errorMessage))
        }
    }

    private suspend fun kotlinx.coroutines.flow.FlowCollector<Resource<ProductDto>>.handleDetailFailure(
        id: Int,
        error: Resource.Error<ProductDto>
    ) {
        val cachedProduct = dao.getProductById(id)
        if (cachedProduct != null) {
            emit(Resource.Success(cachedProduct.toDto()))
        } else {
            emit(error)
        }
    }
}
