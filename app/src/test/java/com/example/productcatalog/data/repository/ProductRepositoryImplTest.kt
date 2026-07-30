package com.example.productcatalog.data.repository

import app.cash.turbine.test
import com.example.productcatalog.data.api.ProductApi
import com.example.productcatalog.data.db.ProductDao
import com.example.productcatalog.data.db.ProductEntity
import com.example.productcatalog.data.model.ProductDto
import com.example.productcatalog.data.model.RatingDto
import com.example.productcatalog.utils.Resource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.io.IOException

class ProductRepositoryImplTest {

    private lateinit var repository: ProductRepositoryImpl
    private val api: ProductApi = mockk()
    private val dao: ProductDao = mockk(relaxed = true)

    private val dummyProductDto = ProductDto(
        id = 1,
        title = "Test Product",
        price = 10.0,
        description = "Test Description",
        category = "Test Category",
        image = "https://test.com/image.png",
        rating = RatingDto(rate = 4.5, count = 10)
    )

    private val dummyProductEntity = ProductEntity(
        id = 1,
        title = "Test Product",
        price = 10.0,
        description = "Test Description",
        category = "Test Category",
        image = "https://test.com/image.png",
        rate = 4.5,
        count = 10
    )

    @Before
    fun setup() {
        repository = ProductRepositoryImpl(api, dao)
    }

    @Test
    fun `getProducts emits loading then success when API call is successful`() = runTest {
        coEvery { api.getProducts() } returns Response.success(listOf(dummyProductDto))

        repository.getProducts().test {
            assertTrue(awaitItem() is Resource.Loading)
            val success = awaitItem() as Resource.Success
            assertEquals(1, success.data?.size)
            assertEquals("Test Product", success.data?.get(0)?.title)
            awaitComplete()
        }

        coVerify { dao.deleteAllProducts() }
        coVerify { dao.insertProducts(any()) }
    }

    @Test
    fun `getProducts emits loading then success from cache when API call fails but cache is not empty`() = runTest {
        coEvery { api.getProducts() } throws IOException()
        coEvery { dao.getProducts() } returns listOf(dummyProductEntity)

        repository.getProducts().test {
            assertTrue(awaitItem() is Resource.Loading)
            val success = awaitItem() as Resource.Success
            assertEquals(1, success.data?.size)
            assertEquals("Test Product", success.data?.get(0)?.title)
            awaitComplete()
        }
    }

    @Test
    fun `getProducts emits loading then error when both API and cache fail`() = runTest {
        coEvery { api.getProducts() } throws IOException()
        coEvery { dao.getProducts() } returns emptyList()

        repository.getProducts().test {
            assertTrue(awaitItem() is Resource.Loading)
            val error = awaitItem() as Resource.Error
            assertTrue(error.message?.contains("Network Error") == true)
            awaitComplete()
        }
    }

    @Test
    fun `getProductDetails emits loading then success when API call is successful`() = runTest {
        coEvery { api.getProductDetails(1) } returns Response.success(dummyProductDto)

        repository.getProductDetails(1).test {
            assertTrue(awaitItem() is Resource.Loading)
            val success = awaitItem() as Resource.Success
            assertEquals("Test Product", success.data?.title)
            awaitComplete()
        }
    }
}
