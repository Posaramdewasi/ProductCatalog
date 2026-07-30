package com.example.productcatalog.data.api

import com.example.productcatalog.data.model.ProductDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApi {

    @GET("products")
    suspend fun getProducts(): Response<List<ProductDto>>

    @GET("products/{id}")
    suspend fun getProductDetails(
        @Path("id") id: Int
    ): Response<ProductDto>
}
