package com.example.productcatalog.ui.detail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.productcatalog.data.model.ProductDto
import com.example.productcatalog.data.model.RatingDto
import com.example.productcatalog.domain.repository.ProductRepository
import com.example.productcatalog.utils.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductDetailViewModelTest {

    private lateinit var viewModel: ProductDetailViewModel
    private val repository: ProductRepository = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()
    private val savedStateHandle: SavedStateHandle = SavedStateHandle(mapOf("product_id" to 1))

    private val dummyProduct = ProductDto(
        id = 1,
        title = "Test Product",
        price = 10.0,
        description = "Test Description",
        category = "Test Category",
        image = "https://test.com/image.png",
        rating = RatingDto(rate = 4.5, count = 10)
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init calls getProductDetails and updates uiState to Success`() = runTest {
        coEvery { repository.getProductDetails(1) } returns flowOf(
            Resource.Loading(),
            Resource.Success(dummyProduct)
        )

        viewModel = ProductDetailViewModel(repository, savedStateHandle)

        viewModel.uiState.test {
            val state = awaitItem() as ProductDetailUiState.Success
            assertEquals("Test Product", state.product.title)
        }
    }

    @Test
    fun `init updates uiState to Error when product not found`() = runTest {
        coEvery { repository.getProductDetails(1) } returns flowOf(
            Resource.Loading(),
            Resource.Error("Product not found")
        )

        viewModel = ProductDetailViewModel(repository, savedStateHandle)

        viewModel.uiState.test {
            val state = awaitItem() as ProductDetailUiState.Error
            assertEquals("Product not found", state.message)
        }
    }
}
